import { api } from "../services/api.js";
import {
  escapeHtml,
  formatDateTime,
  movementBadgeHtml,
  setFeedback
} from "../utils/helpers.js";

function renderLowStock(items) {
  const container = document.querySelector("#low-stock-list");
  if (!container) {
    return;
  }

  if (!items.length) {
    container.innerHTML = `
      <div class="app-empty-state">
        <strong>Nenhum item crítico</strong>
        <span>O estoque atual está saudável.</span>
      </div>
    `;
    return;
  }

  container.innerHTML = items
    .map(
      (item) => `
        <div class="flex items-center justify-between p-4 bg-orange-50 border border-orange-200 rounded-lg">
          <div class="flex-1">
            <p class="font-medium text-gray-900">${escapeHtml(item.produtoNome)}</p>
            <p class="text-sm text-gray-600">Fornecedor: ${escapeHtml(
              item.fornecedorNome
            )}</p>
          </div>
          <div class="text-right">
            <p class="text-sm text-gray-600">Quantidade atual</p>
            <p class="text-lg font-bold text-orange-600">${item.estoqueAtual} / ${item.estoqueMinimo}</p>
          </div>
        </div>
      `
    )
    .join("");
}

function renderRecentMovements(items) {
  const tbody = document.querySelector("#recent-movements-body");
  if (!tbody) {
    return;
  }

  if (!items.length) {
    tbody.innerHTML = `
      <tr>
        <td colspan="4">
          <div class="app-empty-state">
            <strong>Sem movimentações</strong>
            <span>As movimentações recentes aparecerão aqui.</span>
          </div>
        </td>
      </tr>
    `;
    return;
  }

  tbody.innerHTML = items
    .map(
      (item) => `
        <tr class="border-b border-gray-100 hover:bg-gray-50 transition-colors">
          <td class="py-3 px-4 text-sm text-gray-900">${escapeHtml(item.produtoNome)}</td>
          <td class="py-3 px-4">${movementBadgeHtml(item.tipo)}</td>
          <td class="py-3 px-4 text-sm text-gray-900">${item.quantidade}</td>
          <td class="py-3 px-4 text-sm text-gray-600">${formatDateTime(item.dataHora)}</td>
        </tr>
      `
    )
    .join("");
}

export async function initDashboard() {
  const feedback = document.querySelector("#dashboard-feedback");
  const totalProducts = document.querySelector("#dashboard-total-products");
  const totalProductsMeta = document.querySelector("#dashboard-total-products-meta");
  const lowStock = document.querySelector("#dashboard-low-stock");
  const lowStockMeta = document.querySelector("#dashboard-low-stock-meta");
  const totalMovements = document.querySelector("#dashboard-total-movements");
  const totalMovementsMeta = document.querySelector("#dashboard-total-movements-meta");

  try {
    setFeedback(feedback, "");
    const [summary, lowStockItems, recentMovements] = await Promise.all([
      api.dashboard.resumo(),
      api.dashboard.estoqueBaixo(),
      api.dashboard.ultimasMovimentacoes(5)
    ]);

    totalProducts.textContent = String(summary.totalProdutos);
    totalProductsMeta.textContent = `${summary.totalProdutos} itens cadastrados`;
    lowStock.textContent = String(summary.produtosEstoqueBaixo);
    lowStockMeta.textContent = "Produtos com estoque crítico";
    totalMovements.textContent = String(summary.totalMovimentacoes);
    totalMovementsMeta.textContent = "Total de registros";

    renderLowStock(lowStockItems);
    renderRecentMovements(recentMovements);
  } catch (error) {
    setFeedback(
      feedback,
      "Não foi possível carregar o dashboard. Verifique se a API está rodando.",
      "error"
    );
  }
}
