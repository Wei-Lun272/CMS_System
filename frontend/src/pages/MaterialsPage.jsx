import React, { useEffect, useState } from "react";

export default function MaterialsPage() {
  const [materials, setMaterials] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchMaterials = async () => {
      try {
        const response = await fetch("http://localhost:8080/materials"); // 確保後端正確埠號
        if (!response.ok) {
          throw new Error("Failed to fetch materials");
        }
        const data = await response.json();
        setMaterials(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchMaterials();
  }, []);

  return (
    <div>
      <h1>Materials Page</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <ul>
        {materials.map((material) => (
          <li key={material.id}>
            {material.name} - {material.stock}
          </li>
        ))}
      </ul>
    </div>
  );
}
