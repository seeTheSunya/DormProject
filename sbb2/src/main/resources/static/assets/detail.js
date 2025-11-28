function getQueryParam(name) {
  const params = new URLSearchParams(window.location.search);
  return params.get(name);
}

async function loadDetail() {
  const id = getQueryParam("id");

  const res = await fetch(`/api/posts/${id}`);
  const p = await res.json();

  document.getElementById("detail-title").textContent = p.title;
  document.getElementById("detail-writer").textContent = p.writer;
  document.getElementById("detail-date").textContent = p.createdAt
    .replace("T", " ")
    .slice(0, 16);
  document.getElementById("detail-content").textContent = p.content;
  document.getElementById("like-count").textContent = p.likeCount;

  document.getElementById("like-btn").onclick = async () => {
    const res2 = await fetch(`/api/posts/${id}/like`, { method: "POST" });
    const data = await res2.json();
    document.getElementById("like-count").textContent = data.likeCount;
  };
}

document.addEventListener("DOMContentLoaded", loadDetail);
