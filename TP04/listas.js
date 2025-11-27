// localStorage usado:
// produtos = [{nome, descricao, GTIN}]
// listas = [{ nome: "Lista X", produtos: [produtoIndex, produtoIndex...] }]


// Criar lista
document.getElementById("formLista").addEventListener("submit", function(e) {
    e.preventDefault();

    const nomeLista = document.getElementById("nomeLista").value.trim();
    if (!nomeLista) {
        Swal.fire("Erro!", "Digite o nome da lista!", "error");
        return;
    }

    let listas = JSON.parse(localStorage.getItem("listas")) || [];

    // verifica duplicação
    if (listas.some(l => l.nome.toLowerCase() === nomeLista.toLowerCase())) {
        Swal.fire("Erro!", "Já existe uma lista com esse nome!", "error");
        return;
    }

    listas.push({ nome: nomeLista, produtos: [] });

    localStorage.setItem("listas", JSON.stringify(listas));

    Swal.fire("Sucesso!", "Lista criada!", "success");

    document.getElementById("formLista").reset();
    mostrarListas();
});


// Mostrar todas as listas
function mostrarListas() {
    let listas = JSON.parse(localStorage.getItem("listas")) || [];
    let produtos = JSON.parse(localStorage.getItem("produtos")) || [];

    const area = document.getElementById("areaListas");
    area.innerHTML = "";

    listas.forEach((lista, indexLista) => {
        const div = document.createElement("div");
        div.classList.add("lista-box");

        // parte do topo da lista
        div.innerHTML = `
            <h3>${lista.nome}</h3>

            <label>Adicionar produto:</label>
            <select id="sel-${indexLista}">
                <option value="">Selecione...</option>
                ${produtos.map((p, i) => `<option value="${i}">${p.nome}</option>`).join("")}
            </select>
            <button class="btn-add" data-i="${indexLista}">Adicionar</button>

            <button class="btn-delete-lista" data-i="${indexLista}" style="background:#d9534f;color:#fff;margin-left:10px">
                Excluir Lista
            </button>

            <h4>Produtos na lista:</h4>
            <ul id="ul-${indexLista}"></ul>
        `;

        area.appendChild(div);

        // exibir produtos da lista
        const ul = div.querySelector(`#ul-${indexLista}`);
        lista.produtos.forEach(prodIndex => {
            const li = document.createElement("li");

            // se o produto ainda existe no CRUD
            if (produtos[prodIndex]) {
                const p = produtos[prodIndex];

                li.innerHTML = `
                    <strong>${p.nome}</strong><br>
                    <small>Descrição: ${p.descricao}</small><br>
                    <small>GTIN: ${p.GTIN}</small>
                    <br>
                    <button class="btn-rem" data-lista="${indexLista}" data-prod="${prodIndex}">
                        Remover
                    </button>
                `;
            } 
            else {
                // produto foi excluído
                li.innerHTML = `
                    <strong style="color:#a00;">Produto excluído</strong><br>
                    <small>Descrição: —</small><br>
                    <small>GTIN: —</small>
                    <br>
                    <button class="btn-rem" data-lista="${indexLista}" data-prod="${prodIndex}">
                        Remover
                    </button>
                `;
            }

            ul.appendChild(li);
        });
    });

    ativarBotoes();
}


// Ativar botões de adicionar/remover
function ativarBotoes() {
    let listas = JSON.parse(localStorage.getItem("listas")) || [];

    // adicionar produto na lista
    document.querySelectorAll(".btn-add").forEach(btn => {
        btn.addEventListener("click", function() {
            const indexLista = this.getAttribute("data-i");
            const select = document.getElementById(`sel-${indexLista}`);
            const prodIndex = select.value;

            if (prodIndex === "") {
                Swal.fire("Erro!", "Selecione um produto!", "error");
                return;
            }

            listas = JSON.parse(localStorage.getItem("listas")) || [];

            if (listas[indexLista].produtos.includes(prodIndex)) {
                Swal.fire("Erro!", "Esse produto já está na lista!", "error");
                return;
            }

            listas[indexLista].produtos.push(Number(prodIndex));
            localStorage.setItem("listas", JSON.stringify(listas));

            mostrarListas();
        });
    });

    // remover produto da lista
    document.querySelectorAll(".btn-rem").forEach(btn => {
        btn.addEventListener("click", function() {
            const listaIndex = this.getAttribute("data-lista");
            const prodIndex = this.getAttribute("data-prod");

            let listas = JSON.parse(localStorage.getItem("listas")) || [];

            listas[listaIndex].produtos = listas[listaIndex].produtos.filter(p => p != prodIndex);

            localStorage.setItem("listas", JSON.stringify(listas));
            mostrarListas();
        });
    });

    // excluir lista inteira
    document.querySelectorAll(".btn-delete-lista").forEach(btn => {
        btn.addEventListener("click", function() {
            const i = this.getAttribute("data-i");

            Swal.fire({
                title: "Excluir lista?",
                text: "Essa ação não poderá ser desfeita.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Sim, excluir!",
                cancelButtonText: "Cancelar"
            }).then((res) => {
                if (res.isConfirmed) {
                    let listas = JSON.parse(localStorage.getItem("listas")) || [];
                    listas.splice(i, 1);

                    localStorage.setItem("listas", JSON.stringify(listas));
                    mostrarListas();

                    Swal.fire("Excluída!", "A lista foi removida.", "success");
                }
            });
        });
    });
}

mostrarListas();
