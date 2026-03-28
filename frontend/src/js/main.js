import { initDashboard } from "./modules/dashboard.js";
import { initFornecedores } from "./modules/fornecedores.js";
import { initMovimentacoes } from "./modules/movimentacoes.js";
import { initProdutos } from "./modules/produtos.js";
import { setCurrentDateLabels } from "./utils/helpers.js";

async function bootstrap() {
  setCurrentDateLabels();

  const page = document.body.dataset.page;

  if (page === "dashboard") {
    await initDashboard();
    return;
  }

  if (page === "produtos") {
    await initProdutos();
    return;
  }

  if (page === "fornecedores") {
    await initFornecedores();
    return;
  }

  if (page === "movimentacoes") {
    await initMovimentacoes();
  }
}

bootstrap().catch((error) => {
  console.error("Falha ao iniciar o frontend", error);
});
