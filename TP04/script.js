let editIndex = null;

document.getElementById("add").addEventListener("click", function(e) {
    e.preventDefault();
    
    const person = Array.from(
        document.querySelectorAll('#formulario input')
    ).reduce((acc, input) => ({ ...acc, [input.id]: input.value }), {});

    let pessoas = JSON.parse(localStorage.getItem('pessoas')) || [];

    if (editIndex === null) {
        pessoas.push(person);
    } else {
        pessoas[editIndex] = person;
        editIndex = null;
        document.getElementById("add").textContent = "Adicionar";
    }

    localStorage.setItem('pessoas', JSON.stringify(pessoas));

    mostrarPessoas();
    document.getElementById("formulario").reset();
});

function mostrarPessoas() {
    const tbody = document.querySelector("tbody");
    tbody.innerHTML = "";

    let pessoas = JSON.parse(localStorage.getItem('pessoas')) || [];

    pessoas.forEach((p, index) => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${p.nome}</td>
            <td>${p.email}</td>
            <td>${p.telefone}</td>
            <td>${p.data}</td>
            <td class="actions">
                <button class="btn-edit" data-i="${index}">Editar</button>
                <button class="btn-delete" data-i="${index}">Excluir</button>
            </td>
        `;

        tbody.appendChild(tr);
    });

    document.querySelectorAll(".btn-edit").forEach(btn => {
        btn.addEventListener("click", function() {
            let i = this.getAttribute("data-i");
            let pessoas = JSON.parse(localStorage.getItem('pessoas')) || [];
            let p = pessoas[i];

            document.getElementById("nome").value = p.nome;
            document.getElementById("email").value = p.email;
            document.getElementById("telefone").value = p.telefone;
            document.getElementById("data").value = p.data;

            editIndex = i;
            document.getElementById("add").textContent = "Salvar";
        });
    });

    document.querySelectorAll(".btn-delete").forEach(btn => {
        btn.addEventListener("click", function() {
            let i = this.getAttribute("data-i");
            let pessoas = JSON.parse(localStorage.getItem('pessoas')) || [];
            pessoas.splice(i, 1);

            localStorage.setItem('pessoas', JSON.stringify(pessoas));
            mostrarPessoas();
            document.getElementById("formulario").reset();
        });
    });
}

mostrarPessoas();
