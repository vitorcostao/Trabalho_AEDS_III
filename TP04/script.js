let editIndex = null;

function mostrarSucesso(msg) {
    Swal.fire("Sucesso!", msg, "success");
}

function mostrarErro(msg) {
    Swal.fire("Erro!", msg, "error");
}

document.getElementById("add").addEventListener("click", function(e) {
    e.preventDefault();

    const person = Array.from(
        document.querySelectorAll('#formulario input')
    ).reduce((acc, input) => ({ ...acc, [input.id]: input.value }), {});

    // validação
    if (!person.nome || !person.email || !person.telefone || !person.data) {
        mostrarErro("Preencha todos os campos!");
        return;
    }

    let pessoas = JSON.parse(localStorage.getItem('pessoas')) || [];

    if (editIndex === null) {
        pessoas.push(person);
        mostrarSucesso("Pessoa cadastrada com sucesso!");
    } else {
        pessoas[editIndex] = person;
        mostrarSucesso("Dados atualizados com sucesso!");
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

    // editar
    document.querySelectorAll(".btn-edit").forEach(btn => {
        btn.addEventListener("click", function() {
            const i = this.getAttribute("data-i");
            const pessoas = JSON.parse(localStorage.getItem('pessoas')) || [];
            const p = pessoas[i];

            document.getElementById("nome").value = p.nome;
            document.getElementById("email").value = p.email;
            document.getElementById("telefone").value = p.telefone;
            document.getElementById("data").value = p.data;

            editIndex = i;
            document.getElementById("add").textContent = "Salvar";
        });
    });

    // excluir
    document.querySelectorAll(".btn-delete").forEach(btn => {
        btn.addEventListener("click", function() {
            let i = this.getAttribute("data-i");

            Swal.fire({
                title: "Tem certeza?",
                text: "Essa ação não poderá ser desfeita.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Sim, excluir!",
                cancelButtonText: "Cancelar"
            }).then(result => {
                if (result.isConfirmed) {
                    let pessoas = JSON.parse(localStorage.getItem('pessoas')) || [];
                    pessoas.splice(i, 1);

                    localStorage.setItem('pessoas', JSON.stringify(pessoas));

                    mostrarSucesso("Registro excluído!");
                    mostrarPessoas();
                    document.getElementById("formulario").reset();
                }
            });
        });
    });
}

mostrarPessoas();
