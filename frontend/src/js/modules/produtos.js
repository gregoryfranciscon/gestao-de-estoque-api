import { api } from "../services/api.js";
import {
  escapeHtml,
  formatCurrency,
  normalizeError,
  openModal,
  setFeedback,
  showEmptyStateRow,
  showLoadingRows,
  showToast,
  statusBadgeHtml
} from "../utils/helpers.js";

let suppliersCache = [];

function renderProducts(products) {
  const tbody = document.querySelector("#products-table-body");
  if (!tbody) {
    return;
  }

  if (!products.length) {
    showEmptyStateRow(
      tbody,
      7,
      "Nenhum produto encontrado",
      "Ajuste a busca ou cadastre um novo produto."
    );
    return;
  }

  tbody.innerHTML = products
    .map((product) => {
      const lowStockIcon =
        product.status === "ESTOQUE_BAIXO"
          ? `
            <svg class="lucide size-4 text-orange-600" viewBox="0 0 24 24" aria-hidden="true">
              <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3"></path>
              <path d="M12 9v4"></path>
              <path d="M12 17h.01"></path>
            </svg>
          `
          : "";

      return `
        <tr class="border-b border-gray-100 hover:bg-gray-50 transition-colors">
          <td class="py-3 px-4 text-sm font-medium text-gray-900">${escapeHtml(product.nome)}</td>
          <td class="py-3 px-4 text-sm text-gray-600">${escapeHtml(product.categoria)}</td>
          <td class="py-3 px-4 text-sm text-gray-900">${formatCurrency(product.preco)}</td>
          <td class="py-3 px-4 text-sm text-gray-900">
            <div class="flex items-center gap-2">
              ${product.estoqueAtual}
              ${lowStockIcon}
            </div>
          </td>
          <td class="py-3 px-4 text-sm text-gray-600">${escapeHtml(product.fornecedorNome)}</td>
          <td class="py-3 px-4">${statusBadgeHtml(product.status)}</td>
          <td class="py-3 px-4">
            <div class="flex items-center justify-center gap-2">
              <button data-action="edit" data-id="${product.id}" class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-all disabled:pointer-events-none disabled:opacity-50 shrink-0 outline-none h-8 rounded-md px-2.5 hover:bg-blue-50 hover:text-blue-600">
                <svg class="lucide size-4" viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M21.174 6.812a1 1 0 0 0-3.986-3.987L3.842 16.174a2 2 0 0 0-.5.83l-1.321 4.352a.5.5 0 0 0 .623.622l4.353-1.32a2 2 0 0 0 .83-.497z"></path>
                  <path d="m15 5 4 4"></path>
                </svg>
              </button>
              <button data-action="delete" data-id="${product.id}" data-name="${escapeHtml(
                product.nome
              )}" class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-all disabled:pointer-events-none disabled:opacity-50 shrink-0 outline-none h-8 rounded-md px-2.5 hover:bg-red-50 hover:text-red-600">
                <svg class="lucide size-4" viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M3 6h18"></path>
                  <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path>
                  <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path>
                  <line x1="10" x2="10" y1="11" y2="17"></line>
                  <line x1="14" x2="14" y1="11" y2="17"></line>
                </svg>
              </button>
            </div>
          </td>
        </tr>
      `;
    })
    .join("");
}

async function ensureSuppliers() {
  if (!suppliersCache.length) {
    suppliersCache = await api.fornecedores.listar();
  }
  return suppliersCache;
}

function buildProductForm(product = null) {
  const options = suppliersCache
    .map(
      (supplier) => `
        <option value="${supplier.id}" ${
          product?.fornecedorId === supplier.id ? "selected" : ""
        }>${escapeHtml(supplier.nome)}</option>
      `
    )
    .join("");

  return `
    <div class="app-form-grid">
      <div class="app-field app-field-full">
        <label for="product-name">Nome</label>
        <input id="product-name" name="nome" class="app-input" value="${escapeHtml(
          product?.nome || ""
        )}" required />
      </div>
      <div class="app-field">
        <label for="product-category">Categoria</label>
        <input id="product-category" name="categoria" class="app-input" value="${escapeHtml(
          product?.categoria || ""
        )}" required />
      </div>
      <div class="app-field">
        <label for="product-price">Preço</label>
        <input id="product-price" name="preco" type="number" step="0.01" min="0" class="app-input" value="${
          product?.preco ?? ""
        }" required />
      </div>
      <div class="app-field">
        <label for="product-stock">Estoque Atual</label>
        <input id="product-stock" name="estoqueAtual" type="number" min="0" class="app-input" value="${
          product?.estoqueAtual ?? 0
        }" required />
      </div>
      <div class="app-field">
        <label for="product-min-stock">Estoque Mínimo</label>
        <input id="product-min-stock" name="estoqueMinimo" type="number" min="0" class="app-input" value="${
          product?.estoqueMinimo ?? 0
        }" required />
      </div>
      <div class="app-field app-field-full">
        <label for="product-supplier">Fornecedor</label>
        <select id="product-supplier" name="fornecedorId" class="app-select" required>
          <option value="">Selecione um fornecedor</option>
          ${options}
        </select>
      </div>
    </div>
  `;
}

async function openProductModal(product, reload) {
  await ensureSuppliers();

  openModal({
    title: product ? "Editar Produto" : "Cadastrar Produto",
    submitLabel: product ? "Salvar Alterações" : "Cadastrar Produto",
    html: buildProductForm(product),
    onSubmit: async ({ formData, close, setError }) => {
      const payload = {
        nome: formData.get("nome")?.trim(),
        categoria: formData.get("categoria")?.trim(),
        preco: Number(formData.get("preco")),
        estoqueAtual: Number(formData.get("estoqueAtual")),
        estoqueMinimo: Number(formData.get("estoqueMinimo")),
        fornecedorId: Number(formData.get("fornecedorId"))
      };

      try {
        if (product) {
          await api.produtos.atualizar(product.id, payload);
          showToast("Produto atualizado com sucesso");
        } else {
          await api.produtos.criar(payload);
          showToast("Produto cadastrado com sucesso");
        }

        close();
        await reload();
      } catch (error) {
        setError(normalizeError(error));
      }
    }
  });
}

export async function initProdutos() {
  const feedback = document.querySelector("#products-feedback");
  const searchInput = document.querySelector("#product-search");
  const createButton = document.querySelector("#create-product-button");
  const tbody = document.querySelector("#products-table-body");
  let searchDebounce;

  const loadProducts = async () => {
    showLoadingRows(tbody, 7, "Carregando produtos...");
    try {
      setFeedback(feedback, "");
      const products = await api.produtos.listar(searchInput.value.trim());
      renderProducts(products);
    } catch (error) {
      setFeedback(feedback, normalizeError(error), "error");
      showEmptyStateRow(
        tbody,
        7,
        "Falha ao carregar produtos",
        "Verifique se a API está rodando e tente novamente."
      );
    }
  };

  createButton.addEventListener("click", async () => {
    try {
      await openProductModal(null, loadProducts);
    } catch (error) {
      setFeedback(feedback, normalizeError(error), "error");
    }
  });

  searchInput.addEventListener("input", () => {
    window.clearTimeout(searchDebounce);
    searchDebounce = window.setTimeout(loadProducts, 300);
  });

  tbody.addEventListener("click", async (event) => {
    const button = event.target.closest("button[data-action]");
    if (!button) {
      return;
    }

    const id = Number(button.dataset.id);

    if (button.dataset.action === "edit") {
      try {
        const product = await api.produtos.buscarPorId(id);
        await openProductModal(product, loadProducts);
      } catch (error) {
        setFeedback(feedback, normalizeError(error), "error");
      }
      return;
    }

    if (button.dataset.action === "delete") {
      const confirmed = window.confirm(
        `Deseja remover o produto "${button.dataset.name}"?`
      );

      if (!confirmed) {
        return;
      }

      try {
        await api.produtos.remover(id);
        showToast("Produto removido com sucesso");
        await loadProducts();
      } catch (error) {
        setFeedback(feedback, normalizeError(error), "error");
      }
    }
  });

  await loadProducts();
}
