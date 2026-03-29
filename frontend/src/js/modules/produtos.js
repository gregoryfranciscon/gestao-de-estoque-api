const btnCadastrarProduto = document.getElementById("btn-cadastrar");

async function testarApiProdutos() {
  try {
    const produtos = await apiGet("/produtos");
    alert(JSON.stringify(produtos, null, 2));
  } catch (error) {
    console.error("Erro ao buscar produtos:", error);
    alert(`Erro ao buscar /produtos: ${error.message}`);
  }
}


if (btnCadastrarProduto) {
  btnCadastrarProduto.addEventListener("click", testarApiProdutos);
}
