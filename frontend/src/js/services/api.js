const API_BASE_URL =
  window.CONTROLEESTOQUE_API_URL ||
  localStorage.getItem("controleestoque.apiBaseUrl") ||
  "http://localhost:8080/api";

function buildUrl(path, query = {}) {
  const normalizedPath = path.startsWith("/") ? path : `/${path}`;
  const url = new URL(`${API_BASE_URL}${normalizedPath}`);

  Object.entries(query).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") {
      url.searchParams.set(key, value);
    }
  });

  return url.toString();
}

async function parseResponse(response) {
  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get("content-type") || "";
  if (contentType.includes("application/json")) {
    return response.json();
  }

  return response.text();
}

async function request(path, options = {}) {
  const { query, body, headers, ...rest } = options;
  const response = await fetch(buildUrl(path, query), {
    ...rest,
    headers: {
      Accept: "application/json",
      ...(body ? { "Content-Type": "application/json" } : {}),
      ...headers
    },
    body: body ? JSON.stringify(body) : undefined
  });

  const data = await parseResponse(response);

  if (!response.ok) {
    const message =
      (data && typeof data === "object" && (data.message || data.error)) ||
      `Falha na requisicao (${response.status})`;
    const error = new Error(message);
    error.response = data;
    error.status = response.status;
    throw error;
  }

  return data;
}

export const api = {
  dashboard: {
    resumo: () => request("/dashboard/resumo"),
    estoqueBaixo: () => request("/dashboard/estoque-baixo"),
    ultimasMovimentacoes: (limit = 5) =>
      request("/dashboard/ultimas-movimentacoes", { query: { limit } })
  },
  produtos: {
    listar: (nome = "") => request("/produtos", { query: { nome } }),
    buscarPorId: (id) => request(`/produtos/${id}`),
    criar: (payload) => request("/produtos", { method: "POST", body: payload }),
    atualizar: (id, payload) =>
      request(`/produtos/${id}`, { method: "PUT", body: payload }),
    remover: (id) => request(`/produtos/${id}`, { method: "DELETE" })
  },
  fornecedores: {
    listar: () => request("/fornecedores"),
    buscarPorId: (id) => request(`/fornecedores/${id}`),
    criar: (payload) =>
      request("/fornecedores", { method: "POST", body: payload }),
    atualizar: (id, payload) =>
      request(`/fornecedores/${id}`, { method: "PUT", body: payload }),
    remover: (id) => request(`/fornecedores/${id}`, { method: "DELETE" })
  },
  movimentacoes: {
    listar: (filters = {}) => request("/movimentacoes", { query: filters }),
    criar: (payload) =>
      request("/movimentacoes", { method: "POST", body: payload })
  }
};
