async function loadMemories() {
  let res = await fetch('/api/memories');
  let data = await res.json();
  document.getElementById("memoryList").innerHTML = data.map(m =>
    `<div style="border:1px solid #ccc; margin:5px; padding:5px;">
       <img src="${m.photoUrl}" width="150"/><br>
       <b>${m.date} - ${m.title}</b><br>
       ${m.message}<br>
       <i>by ${m.author}</i>
     </div>`
  ).join('');
}

document.getElementById("memoryForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  const memory = Object.fromEntries(formData.entries());
  await fetch('/api/memories', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(memory)
  });
  e.target.reset();
  loadMemories();
});

loadMemories();
