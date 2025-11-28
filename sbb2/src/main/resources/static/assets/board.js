// assets/board.js

const CATEGORY_LABELS = {
  group: "공구 / 나눔",
  review: "상품 후기",
  recipe: "기숙사 레시피",
  tip: "생활 꿀팁",
  counseling: "고민 상담소",
};

const CATEGORY_DESCS = {
  group: "기숙사 친구들과 같이 공구하고 나누는 공간입니다.",
  review: "기숙사에서 써본 각종 물건들의 후기를 공유해요.",
  recipe: "전자레인지, 인덕션으로 할 수 있는 레시피 모음.",
  tip: "청소, 관리, 생활비 절약 등 꿀팁을 공유해요.",
  counseling: "익명으로 고민을 나누고 서로 위로해주는 공간입니다.",
};

// URL 파라미터 읽기
function getQueryParam(name) {
  const params = new URLSearchParams(window.location.search);
  return params.get(name);
}

// 메인 페이지용
async function loadMainPage() {
  const username = localStorage.getItem("loginUser") || "게스트";
  const nameSpan = document.getElementById("main-username");
  if (nameSpan) nameSpan.textContent = username;

  try {
    const res = await fetch("/api/posts?size=5"); // 전체 최신 몇 개
    if (!res.ok) return;

    const posts = await res.json();
    // posts는 [{id, category, title, ...}, ...] 형태라고 가정
    ["group", "review", "recipe", "tip", "counseling"].forEach((cat) => {
      const box = document.getElementById("preview-" + cat);
      if (!box) return;
      const filtered = posts.filter((p) => p.category === cat);

      if (filtered.length === 0) {
        box.innerHTML =
          '<div class="board-card-empty">아직 글이 없습니다. 첫 글을 작성해보세요!</div>';
      } else {
        const top = filtered.slice(0, 3);
        box.innerHTML =
          "<ul style='list-style:none; padding-left:0; margin:0; display:flex; flex-direction:column; gap:6px;'>" +
          top
            .map(
              (p) =>
                `<li style="font-size:13px; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
                   • ${p.title}
                 </li>`
            )
            .join("") +
          "</ul>";
      }
    });
  } catch (e) {
    console.error(e);
  }
}

// 목록 페이지용
async function loadBoardPage() {
  const username = localStorage.getItem("loginUser") || "게스트";
  const nameSpan = document.getElementById("board-username");
  if (nameSpan) nameSpan.textContent = username;

  // 기본 카테고리: URL ?category=xxx 있으면 사용
  const catParam = getQueryParam("category") || "all";
  changeCategory(catParam);
}

async function loadPostsByCategory(category) {
  let url = "/api/posts";
  if (category && category !== "all") {
    url += "?category=" + encodeURIComponent(category);
  }
  const res = await fetch(url);
  if (!res.ok) {
    return [];
  }
  return await res.json();
}

function setBoardHeader(category) {
  const titleEl = document.getElementById("board-title");
  const descEl = document.getElementById("board-desc");

  if (category === "all") {
    if (titleEl) titleEl.textContent = "전체 게시글";
    if (descEl)
      descEl.textContent = "카테고리 상관 없이 모든 글을 한눈에 볼 수 있어요.";
  } else {
    if (titleEl) titleEl.textContent = CATEGORY_LABELS[category] || "게시판";
    if (descEl) descEl.textContent = CATEGORY_DESCS[category] || "";
  }
}

async function changeCategory(category) {
  // pill 버튼 active 표시
  document
    .querySelectorAll(".pill-btn")
    .forEach((btn) => btn.classList.remove("active"));
  const activeBtn = document.querySelector(`.pill-btn[data-cat="${category}"]`);
  if (activeBtn) activeBtn.classList.add("active");

  setBoardHeader(category);

  const tbody = document.getElementById("board-tbody");
  if (!tbody) return;

  tbody.innerHTML =
    '<tr class="board-empty-row"><td colspan="5">게시글을 불러오는 중입니다...</td></tr>';

  try {
    const posts = await loadPostsByCategory(category);

    if (!posts || posts.length === 0) {
      tbody.innerHTML =
        '<tr class="board-empty-row"><td colspan="5">작성된 글이 없습니다.</td></tr>';
      return;
    }

    tbody.innerHTML = posts
      .map((p, idx) => {
        const date =
          p.createdAt != null
            ? new Date(p.createdAt).toLocaleString("ko-KR")
            : "";
        const author = p.authorName || (p.member && p.member.username) || "익명";
        const likes = p.likes != null ? p.likes : 0;
        return `
          <tr onclick="goDetail(${p.id})">
            <td>${idx + 1}</td>
            <td class="title-cell">${p.title}</td>
            <td>${author}</td>
            <td>${date}</td>
            <td>${likes}</td>
          </tr>
        `;
      })
      .join("");
  } catch (e) {
    console.error(e);
    tbody.innerHTML =
      '<tr class="board-empty-row"><td colspan="5">게시글을 불러오는 중 오류가 발생했습니다.</td></tr>';
  }
}

function goBoard(category) {
  const url = category
    ? `board.html?category=${encodeURIComponent(category)}`
    : "board.html";
  location.href = url;
}

function goWrite() {
  const params = new URLSearchParams(window.location.search);
  const cat = params.get("category") || "group";
  location.href = "write.html?category=" + encodeURIComponent(cat);
}

function goDetail(id) {
  location.href = "detail.html?id=" + encodeURIComponent(id);
}
