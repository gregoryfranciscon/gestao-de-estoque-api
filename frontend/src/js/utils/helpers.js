function ensureToastContainer() {
  let container = document.querySelector("[data-toast-container]");
  if (!container) {
    container = document.createElement("div");
    container.className = "app-toast-container";
    container.dataset.toastContainer = "true";
    document.body.appendChild(container);
  }
  return container;
}

export function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;");
}

export function formatCurrency(value) {
  return new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL"
  }).format(Number(value || 0));
}

export function formatDateTime(value) {
  if (!value) {
    return "-";
  }

  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "medium"
  }).format(new Date(value));
}

export function formatDateLabel(date = new Date()) {
  return new Intl.DateTimeFormat("pt-BR", {
    weekday: "long",
    day: "2-digit",
    month: "long",
    year: "numeric"
  }).format(date);
}

export function formatForDateTimeLocal(date = new Date()) {
  const pad = (value) => String(value).padStart(2, "0");
  return [
    date.getFullYear(),
    pad(date.getMonth() + 1),
    pad(date.getDate())
  ].join("-") + `T${pad(date.getHours())}:${pad(date.getMinutes())}`;
}

export function setCurrentDateLabels() {
  document.querySelectorAll("[data-current-date]").forEach((element) => {
    element.textContent = formatDateLabel();
  });
}

export function normalizeError(error) {
  if (!error) {
    return "Erro inesperado";
  }

  if (error.response?.validationErrors) {
    return Object.values(error.response.validationErrors).join(" | ");
  }

  return error.message || "Erro inesperado";
}

export function setFeedback(element, message, type = "error") {
  if (!element) {
    return;
  }

  if (!message) {
    element.textContent = "";
    element.className = "app-feedback";
    return;
  }

  element.textContent = message;
  element.className = `app-feedback is-visible ${
    type === "success" ? "is-success" : "is-error"
  }`;
}

export function showToast(message, type = "success") {
  const container = ensureToastContainer();
  const toast = document.createElement("div");
  toast.className = `app-toast ${
    type === "error" ? "app-toast-error" : "app-toast-success"
  }`;
  toast.textContent = message;
  container.appendChild(toast);

  window.setTimeout(() => {
    toast.remove();
  }, 3200);
}

export function showLoadingRows(tbody, colspan, message = "Carregando...") {
  if (!tbody) {
    return;
  }

  tbody.innerHTML = `
    <tr>
      <td colspan="${colspan}" class="app-empty-state">${escapeHtml(message)}</td>
    </tr>
  `;
}

export function showEmptyStateRow(tbody, colspan, title, description) {
  if (!tbody) {
    return;
  }

  tbody.innerHTML = `
    <tr>
      <td colspan="${colspan}">
        <div class="app-empty-state">
          <strong>${escapeHtml(title)}</strong>
          <span>${escapeHtml(description)}</span>
        </div>
      </td>
    </tr>
  `;
}

export function statusBadgeHtml(status) {
  const isLow = status === "ESTOQUE_BAIXO";
  return `
    <span class="inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 gap-1 transition-[color,box-shadow] overflow-hidden ${
      isLow
        ? "bg-orange-100 text-orange-800 hover:bg-orange-100"
        : "bg-green-100 text-green-800 hover:bg-green-100"
    }">
      ${isLow ? "Estoque Baixo" : "Normal"}
    </span>
  `;
}

export function movementBadgeHtml(tipo) {
  const isEntrada = tipo === "ENTRADA";
  const colorClasses = isEntrada
    ? "bg-green-100 text-green-800 hover:bg-green-100"
    : "bg-red-100 text-red-800 hover:bg-red-100";
  const icon = isEntrada
    ? '<svg class="lucide size-3" viewBox="0 0 24 24" aria-hidden="true"><path d="m5 12 7-7 7 7"></path><path d="M12 19V5"></path></svg>'
    : '<svg class="lucide size-3" viewBox="0 0 24 24" aria-hidden="true"><path d="M12 5v14"></path><path d="m19 12-7 7-7-7"></path></svg>';

  return `
    <span class="inline-flex items-center justify-center rounded-md border px-2 py-0.5 text-xs font-medium w-fit whitespace-nowrap shrink-0 gap-1 transition-[color,box-shadow] overflow-hidden ${colorClasses}">
      ${icon}
      ${isEntrada ? "Entrada" : "Saída"}
    </span>
  `;
}

export function openModal({ title, html, submitLabel, onSubmit }) {
  const overlay = document.createElement("div");
  overlay.className = "app-modal-backdrop";
  overlay.innerHTML = `
    <div class="app-modal-panel" role="dialog" aria-modal="true" aria-label="${escapeHtml(
      title
    )}">
      <div class="app-modal-header">
        <h3 class="app-modal-title">${escapeHtml(title)}</h3>
        <button type="button" class="app-modal-close" aria-label="Fechar">×</button>
      </div>
      <div class="app-modal-body">
        <div class="app-feedback" data-modal-feedback></div>
        <form data-modal-form>
          ${html}
          <div class="app-modal-actions">
            <button type="button" class="app-button-secondary" data-cancel-button>Cancelar</button>
            <button type="submit" class="app-button-primary bg-blue-600 text-white hover:bg-blue-700" data-submit-button>${escapeHtml(
              submitLabel
            )}</button>
          </div>
        </form>
      </div>
    </div>
  `;

  const close = () => {
    document.removeEventListener("keydown", handleKeydown);
    overlay.remove();
  };

  const handleKeydown = (event) => {
    if (event.key === "Escape") {
      close();
    }
  };

  document.addEventListener("keydown", handleKeydown);
  document.body.appendChild(overlay);

  const form = overlay.querySelector("[data-modal-form]");
  const feedback = overlay.querySelector("[data-modal-feedback]");
  const submitButton = overlay.querySelector("[data-submit-button]");

  const setError = (message) => setFeedback(feedback, message, "error");

  overlay.addEventListener("click", (event) => {
    if (event.target === overlay) {
      close();
    }
  });

  overlay.querySelector(".app-modal-close").addEventListener("click", close);
  overlay.querySelector("[data-cancel-button]").addEventListener("click", close);

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    setFeedback(feedback, "");
    submitButton.disabled = true;

    try {
      await onSubmit({
        form,
        formData: new FormData(form),
        close,
        setError,
        submitButton
      });
    } finally {
      submitButton.disabled = false;
    }
  });

  return { overlay, form, close, setError, submitButton };
}
