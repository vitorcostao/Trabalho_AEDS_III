let editIndex = null;

function mostrarSucesso(msg) {
    Swal.fire("Sucesso!", msg, "success");
}

function mostrarErro(msg) {
    Swal.fire("Erro!", msg, "error");
}

document.getElementById("add").addEventListener("click", function(e) {
    e.preventDefault();

    const produto = {
        nome: document.getElementById("nome").value.trim(),
        descricao: document.getElementById("desc").value.trim(),
        GTIN: document.getElementById("GTIN").value.trim()
    };

    if (!produto.nome || !produto.descricao || !produto.GTIN) {
        mostrarErro("Preencha todos os campos!");
        return;
    }

    let produtos = JSON.parse(localStorage.getItem('produtos')) || [];

    if (editIndex === null) {
        produtos.push(produto);
        mostrarSucesso("Produto cadastrado com sucesso!");
    } else {
        produtos[editIndex] = produto;
        mostrarSucesso("Produto atualizado com sucesso!");
        editIndex = null;
        document.getElementById("add").textContent = "Adicionar";
    }

    localStorage.setItem('produtos', JSON.stringify(produtos));

    mostrarProdutos();
    document.getElementById("formulario").reset();
});

function mostrarProdutos(filtro = "") {
    const tbody = document.querySelector("tbody");
    tbody.innerHTML = "";

    let produtos = JSON.parse(localStorage.getItem('produtos')) || [];


    produtos = produtos.filter(p => 
        p.nome.toLowerCase().includes(filtro.toLowerCase())
    );

    produtos.forEach((p, index) => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${p.nome}</td>
            <td>${p.descricao}</td>
            <td>${p.GTIN}</td>
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
            const produtos = JSON.parse(localStorage.getItem('produtos')) || [];
            const p = produtos[i];

            document.getElementById("nome").value = p.nome;
            document.getElementById("desc").value = p.descricao;
            document.getElementById("GTIN").value = p.GTIN;

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
                    let produtos = JSON.parse(localStorage.getItem('produtos')) || [];
                    produtos.splice(i, 1);

                    localStorage.setItem('produtos', JSON.stringify(produtos));

                    mostrarSucesso("Produto excluído!");
                    mostrarProdutos();
                    document.getElementById("formulario").reset();
                }
            });
        });
    });
}

document.getElementById("buscar").addEventListener("input", function() {
    mostrarProdutos(this.value);
});

mostrarProdutos();
