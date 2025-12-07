// assets/post.js

const CATEGORY_LABELS = {
  group: "공구 / 나눔",
  review: "상품 후기",
  recipe: "기숙사 레시피",
  tip: "생활 꿀팁",
  counseling: "고민 상담소",
};

function getQueryParam(name) {
  const params = new URLSearchParams(window.location.search);
  return params.get(name);
}

/* ===========================
   글쓰기 페이지
=========================== */

function initWritePage() {
  const username = localStorage.getItem("loginUser") || "게스트";
  const nameSpan = document.getElementById("write-username");
  if (nameSpan) nameSpan.textContent = username;

  // URL에 category가 있으면 select에 반영
  const catParam = getQueryParam("category");
  if (catParam) {
    const sel = document.getElementById("write-category");
    if (sel) sel.value = catParam;
  }
}

async function handleWriteSubmit(e) {
  e.preventDefault();
  const category = document.getElementById("write-category").value;
  const title = document.getElementById("write-title").value.trim();
  const content = document.getElementById("write-content").value.trim();
  const imageUrl = document.getElementById("write-image").value.trim();
  const msg = document.getElementById("writeMessage");

  msg.textContent = "";
  msg.className = "message";

  if (!title || !content) {
    msg.textContent = "제목과 내용을 모두 입력해주세요.";
    msg.classList.add("message-error");
    return;
  }

  const username = localStorage.getItem("loginUser") || "익명";

  try {
    const res = await fetch("/api/posts", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        category,
        title,
        content,
        imageUrl: imageUrl || null,
        username,
      }),
    });

    if (!res.ok) {
      const text = await res.text();
      msg.textContent = text || "글 등록에 실패했습니다.";
      msg.classList.add("message-error");
      return;
    }

    const data = await res.json();
    msg.textContent = "글이 등록되었습니다.";
    msg.classList.add("message-ok");

    // 등록 후 해당 카테고리 목록으로 이동
    setTimeout(() => {
      location.href =
        "board.html?category=" + encodeURIComponent(data.category || category);
    }, 800);
  } catch (e) {
    console.error(e);
    msg.textContent = "서버 오류가 발생했습니다.";
    msg.classList.add("message-error");
  }
}

/* ===========================
   상세 페이지
=========================== */

async function loadPostDetail() {
  const username = localStorage.getItem("loginUser") || "게스트";
  const nameSpan = document.getElementById("detail-username");
  if (nameSpan) nameSpan.textContent = username;

  const id = getQueryParam("id");
  if (!id) {
    alert("잘못된 접근입니다.");
    history.back();
    return;
  }

  try {
    const res = await fetch("/api/posts/" + id);
    if (!res.ok) {
      alert("게시글을 불러오지 못했습니다.");
      history.back();
      return;
    }

    const p = await res.json();

    document.getElementById("detail-title").textContent = p.title;
    document.getElementById("detail-content").textContent = p.content;
    document.getElementById("detail-category").textContent =
      CATEGORY_LABELS[p.category] || p.category;
    document.getElementById("detail-author").textContent =
      p.authorName || (p.member && p.member.username) || "익명";
    document.getElementById("detail-date").textContent =
      p.createdAt != null
        ? new Date(p.createdAt).toLocaleString("ko-KR")
        : "";

    const likeCount = p.likes != null ? p.likes : 0;
    document.getElementById("like-count").textContent = likeCount;

    loadComments(id);
  } catch (e) {
    console.error(e);
  }
}

/* ===========================
   하트 / 좋아요
=========================== */

async function toggleLike() {
  const id = getQueryParam("id");
  if (!id) return;

  try {
    const res = await fetch(`/api/posts/${id}/like`, {
      method: "POST",
    });
    if (!res.ok) return;

    const data = await res.json();
    const likeCount = data.likes != null ? data.likes : 0;
    document.getElementById("like-count").textContent = likeCount;
  } catch (e) {
    console.error(e);
  }
}

/* ===========================
   댓글
=========================== */

async function loadComments(postId) {
  try {
    const res = await fetch(`/api/posts/${postId}/comments`);
    if (!res.ok) return;
    const list = await res.json();

    const box = document.getElementById("comment-list");
    if (!box) return;

    if (!list || list.length === 0) {
      box.innerHTML =
        '<div class="comment-item">첫 댓글을 남겨보세요.</div>';
      return;
    }

    box.innerHTML = list
      .map((c) => {
        const date =
          c.createdAt != null
            ? new Date(c.createdAt).toLocaleString("ko-KR")
            : "";
        return `
          <div class="comment-item">
            <div class="comment-meta">
              ${c.authorName || "익명"} · ${date}
            </div>
            <div>${c.content}</div>
          </div>
        `;
      })
      .join("");
  } catch (e) {
    console.error(e);
  }
}

async function handleAddComment(e) {
  e.preventDefault();
  const postId = getQueryParam("id");
  if (!postId) return;

  const content = document
    .getElementById("comment-content")
    .value.trim();
  const msg = document.getElementById("commentMessage");
  const username = localStorage.getItem("loginUser") || "익명";

  msg.textContent = "";
  msg.className = "message";

  if (!content) {
    msg.textContent = "댓글 내용을 입력하세요.";
    msg.classList.add("message-error");
    return;
  }

  try {
    const res = await fetch(`/api/posts/${postId}/comments`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ content, username }),
    });

    if (!res.ok) {
      msg.textContent = "댓글 등록에 실패했습니다.";
      msg.classList.add("message-error");
      return;
    }

    document.getElementById("comment-content").value = "";
    msg.textContent = "댓글이 등록되었습니다.";
    msg.classList.add("message-ok");
    loadComments(postId);
  } catch (e) {
    console.error(e);
    msg.textContent = "서버 오류가 발생했습니다.";
    msg.classList.add("message-error");
  }
}
