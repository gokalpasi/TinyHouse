// Kayıt formu gönderimi
document.getElementById("registerForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const data = {
        fullName: document.getElementById("fullName").value,
        email: document.getElementById("registerEmail").value,
        passwordHash: document.getElementById("registerPassword").value,
        role: document.getElementById("role").value,
        isActive: true
    };

    fetch("http://localhost:8080/api/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(res => res.text())
        .then(msg => alert(msg))
        .catch(err => alert("Kayıt sırasında hata oluştu."));
});

// Giriş formu gönderimi
document.getElementById("loginForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const data = {
        email: document.getElementById("loginEmail").value,
        password: document.getElementById("loginPassword").value
    };

    fetch("http://localhost:8080/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(res => res.text())
        .then(msg => alert(msg))
        .catch(err => alert("Giriş sırasında hata oluştu."));
});
