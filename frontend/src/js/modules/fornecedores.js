import { api } from "../services/api.js";
import {
  escapeHtml,
  normalizeError,
  openModal,
  setFeedback,
  showEmptyStateRow,
  showLoadingRows,
  showToast
} from "../utils/helpers.js";

function renderSuppliers(suppliers) {
  const tbody = document.querySelector("#suppliers-table-body");
  if (!tbody) {
    return;
  }

  if (!suppliers.length) {
    showEmptyStateRow(
      tbody,
      4,
      "Nenhum fornecedor encontrado",
      "Cadastre um fornecedor para vinculá-lo aos produtos."
    );
    return;
  }

  tbody.innerHTML = suppliers
    .map(
      (supplier) => `
        <tr class="border-b border-gray-100 hover:bg-gray-50 transition-colors">
          <td class="py-3 px-4 text-sm font-medium text-gray-900">${escapeHtml(supplier.nome)}</td>
          <td class="py-3 px-4 text-sm text-gray-600">${escapeHtml(supplier.telefone || "-")}</td>
          <td class="py-3 px-4 text-sm text-gray-600">${escapeHtml(supplier.email || "-")}</td>
          <td class="py-3 px-4">
            <div class="flex items-center justify-center gap-2">
              <button data-action="edit" data-id="${supplier.id}" class="inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-all disabled:pointer-events-none disabled:opacity-50 shrink-0 outline-none h-8 rounded-md px-2.5 hover:bg-blue-50 hover:text-blue-600">
                <svg class="lucide size-4" viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M21.174 6.812a1 1 0 0 0-3.986-3.987L3.842 16.174a2 2 0 0 0-.5.83l-1.321 4.352a.5.5 0 0 0 .623.622l4.353-1.32a2 2 0 0 0 .83-.497z"></path>
                  <path d="m15 5 4 4"></path>
                </svg>
              </button>
              <button data-action="delete" data-id="${supplier.id}" data-name="${escapeHtml(
                supplier.nome
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
      `
    )
    .join("");
}

function buildSupplierForm(supplier = null) {
  return `
    <div class="app-form-grid">
      <div class="app-field app-field-full">
        <label for="supplier-name">Nome</label>
        <input id="supplier-name" name="nome" class="app-input" value="${escapeHtml(
          supplier?.nome || ""
        )}" required />
      </div>
      <div class="app-field">
        <label for="supplier-phone">Telefone</label>
        <input id="supplier-phone" name="telefone" class="app-input" value="${escapeHtml(
          supplier?.telefone || ""
        )}" />
      </div>
      <div class="app-field">
        <label for="supplier-email">E-mail</label>
        <input id="supplier-email" name="email" type="email" class="app-input" value="${escapeHtml(
          supplier?.email || ""
        )}" />
      </div>
    </div>
  `;
}

async function openSupplierModal(supplier, reload) {
  openModal({
    title: supplier ? "Editar Fornecedor" : "Cadastrar Fornecedor",
    submitLabel: supplier ? "Salvar Alterações" : "Cadastrar Fornecedor",
    html: buildSupplierForm(supplier),
    onSubmit: async ({ formData, close, setError }) => {
      const payload = {
        nome: formData.get("nome")?.trim(),
        telefone: formData.get("telefone")?.trim(),
        email: formData.get("email")?.trim()
      };

      try {
        if (supplier) {
          await api.fornecedores.atualizar(supplier.id, payload);
          showToast("Fornecedor atualizado com sucesso");
        } else {
          await api.fornecedores.criar(payload);
          showToast("Fornecedor cadastrado com sucesso");
        }

        close();
        await reload();
      } catch (error) {
        setError(normalizeError(error));
      }
    }
  });
}

export async function initFornecedores() {
  const feedback = document.querySelector("#suppliers-feedback");
  const createButton = document.querySelector("#create-supplier-button");
  const tbody = document.querySelector("#suppliers-table-body");

  const loadSuppliers = async () => {
    showLoadingRows(tbody, 4, "Carregando fornecedores...");
    try {
      setFeedback(feedback, "");
      const suppliers = await api.fornecedores.listar();
      renderSuppliers(suppliers);
    } catch (error) {
      setFeedback(feedback, normalizeError(error), "error");
      showEmptyStateRow(
        tbody,
        4,
        "Falha ao carregar fornecedores",
        "Verifique se a API está rodando e tente novamente."
      );
    }
  };

  createButton.addEventListener("click", () => openSupplierModal(null, loadSuppliers));

  tbody.addEventListener("click", async (event) => {
    const button = event.target.closest("button[data-action]");
    if (!button) {
      return;
    }

    const id = Number(button.dataset.id);

    if (button.dataset.action === "edit") {
      try {
        const supplier = await api.fornecedores.buscarPorId(id);
        await openSupplierModal(supplier, loadSuppliers);
      } catch (error) {
        setFeedback(feedback, normalizeError(error), "error");
      }
      return;
    }

    if (button.dataset.action === "delete") {
      const confirmed = window.confirm(
        `Deseja remover o fornecedor "${button.dataset.name}"?`
      );
      if (!confirmed) {
        return;
      }

      try {
        await api.fornecedores.remover(id);
        showToast("Fornecedor removido com sucesso");
        await loadSuppliers();
      } catch (error) {
        setFeedback(feedback, normalizeError(error), "error");
      }
    }
  });

  await loadSuppliers();
}
