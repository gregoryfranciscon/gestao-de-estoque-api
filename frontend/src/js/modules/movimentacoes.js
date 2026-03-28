import { api } from "../services/api.js";
import {
  escapeHtml,
  formatDateTime,
  formatForDateTimeLocal,
  movementBadgeHtml,
  normalizeError,
  setFeedback,
  showEmptyStateRow,
  showLoadingRows,
  showToast
} from "../utils/helpers.js";

let productsCache = [];

function buildProductOptions(products, includeAll = false) {
  const allOption = includeAll
    ? '<option value="">Todos os produtos</option>'
    : '<option value="">Selecione um produto</option>';

  return allOption.concat(
    products
      .map(
        (product) =>
          `<option value="${product.id}">${escapeHtml(product.nome)}</option>`
      )
      .join("")
  );
}

function populateProductSelects() {
  const movementSelect = document.querySelector("#movement-product");
  const filterSelect = document.querySelector("#movement-filter-product");

  if (movementSelect) {
    movementSelect.innerHTML = buildProductOptions(productsCache, false);
  }

  if (filterSelect) {
    const currentValue = filterSelect.value;
    filterSelect.innerHTML = buildProductOptions(productsCache, true);
    filterSelect.value = currentValue;
  }
}

function renderMovements(movements) {
  const tbody = document.querySelector("#movements-table-body");
  if (!tbody) {
    return;
  }

  if (!movements.length) {
    showEmptyStateRow(
      tbody,
      4,
      "Nenhuma movimentação encontrada",
      "Registre uma movimentação ou ajuste os filtros."
    );
    return;
  }

  tbody.innerHTML = movements
    .map(
      (movement) => `
        <tr class="border-b border-gray-100 hover:bg-gray-50 transition-colors">
          <td class="py-3 px-4 text-sm font-medium text-gray-900">${escapeHtml(
            movement.produtoNome
          )}</td>
          <td class="py-3 px-4">${movementBadgeHtml(movement.tipo)}</td>
          <td class="py-3 px-4 text-sm text-gray-900">${movement.quantidade}</td>
          <td class="py-3 px-4 text-sm text-gray-600">${formatDateTime(
            movement.dataHora
          )}</td>
        </tr>
      `
    )
    .join("");
}

async function loadProducts() {
  productsCache = await api.produtos.listar();
  populateProductSelects();
}

export async function initMovimentacoes() {
  const feedback = document.querySelector("#movements-feedback");
  const tbody = document.querySelector("#movements-table-body");
  const form = document.querySelector("#movement-form");
  const movementProduct = document.querySelector("#movement-product");
  const movementType = document.querySelector("#movement-type");
  const movementQuantity = document.querySelector("#movement-quantity");
  const movementDateTime = document.querySelector("#movement-datetime");
  const filterProduct = document.querySelector("#movement-filter-product");
  const filterType = document.querySelector("#movement-filter-type");

  const loadMovements = async () => {
    showLoadingRows(tbody, 4, "Carregando movimentações...");

    try {
      setFeedback(feedback, "");
      const movements = await api.movimentacoes.listar({
        produtoId: filterProduct.value,
        tipo: filterType.value
      });
      renderMovements(movements);
    } catch (error) {
      setFeedback(feedback, normalizeError(error), "error");
      showEmptyStateRow(
        tbody,
        4,
        "Falha ao carregar movimentações",
        "Verifique se a API está rodando e tente novamente."
      );
    }
  };

  movementDateTime.value = formatForDateTimeLocal(new Date());

  try {
    await loadProducts();
  } catch (error) {
    setFeedback(feedback, normalizeError(error), "error");
  }

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    try {
      await api.movimentacoes.criar({
        produtoId: Number(movementProduct.value),
        tipo: movementType.value,
        quantidade: Number(movementQuantity.value),
        dataHora: movementDateTime.value
      });

      showToast("Movimentação registrada com sucesso");
      form.reset();
      movementDateTime.value = formatForDateTimeLocal(new Date());
      await loadProducts();
      await loadMovements();
    } catch (error) {
      setFeedback(feedback, normalizeError(error), "error");
    }
  });

  filterProduct.addEventListener("change", loadMovements);
  filterType.addEventListener("change", loadMovements);

  await loadMovements();
}
