import React, { useEffect, useState } from "react";

function App() {
  const [pets, setPets] = useState([]);
  const [form, setForm] = useState({ name: "", species: "", price: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const loadPets = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await fetch("/api/pets");
      if (!res.ok) throw new Error("Failed to load pets");
      const data = await res.json();
      setPets(data);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPets();
  }, []);

  const onChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const payload = {
        name: form.name,
        species: form.species,
        price: form.price ? Number(form.price) : 0,
      };
      const res = await fetch("/api/pets", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error("Failed to create pet");
      setForm({ name: "", species: "", price: "" });
      await loadPets();
    } catch (e) {
      setError(e.message);
    }
  };

  return (
    <div
      style={{ maxWidth: 800, margin: "2rem auto", fontFamily: "system-ui" }}
    >
      <h1>Pet Shop</h1>
      {error && <div style={{ color: "red" }}>{error}</div>}

      <section style={{ marginBottom: "2rem" }}>
        <h2>Add Pet</h2>
        <form
          onSubmit={onSubmit}
          style={{
            display: "grid",
            gap: "0.5rem",
            gridTemplateColumns: "1fr 1fr 1fr auto",
          }}
        >
          <input
            name="name"
            placeholder="Name"
            value={form.name}
            onChange={onChange}
            required
          />
          <input
            name="species"
            placeholder="Species"
            value={form.species}
            onChange={onChange}
            required
          />
          <input
            name="price"
            type="number"
            min="0"
            step="0.01"
            placeholder="Price"
            value={form.price}
            onChange={onChange}
          />
          <button type="submit">Add</button>
        </form>
      </section>

      <section>
        <h2>Pets {loading ? "(Loading...)" : ""}</h2>
        <table
          border="1"
          cellPadding="8"
          style={{ width: "100%", borderCollapse: "collapse" }}
        >
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Species</th>
              <th>Status</th>
              <th>Price</th>
            </tr>
          </thead>
          <tbody>
            {pets.map((p) => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>{p.name}</td>
                <td>{p.species}</td>
                <td>{p.status}</td>
                <td>{p.price}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </section>
    </div>
  );
}

export default App;
