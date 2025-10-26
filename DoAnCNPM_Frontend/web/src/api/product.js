const PRODUCT_URL = "http://localhost:8080/api/products"; // product-service

export async function getAllProducts() {
  const res = await fetch(PRODUCT_URL);
  return res.json();
}
