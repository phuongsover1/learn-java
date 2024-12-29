import ProductCard from "./ProductCard";

export default function Products({ auth, productList }) {
  return (
    <>
      {productList.map((item) => (
        <ProductCard key={item.id} product={item} auth={auth} />
      ))}
    </>
  );
}
