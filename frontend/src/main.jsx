import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';

function CamereWidget() {
  const [camere, setCamere] = useState([]);
  const [capienzaMinima, setCapienzaMinima] = useState('');
  const [caricamento, setCaricamento] = useState(true);
  const [errore, setErrore] = useState(null);

  useEffect(() => {
    fetch('/api/camere')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Errore nel caricamento');
        }
        return response.json();
      })
      .then((dati) => setCamere(dati))
      .catch(() => setErrore('Impossibile caricare le camere.'))
      .finally(() => setCaricamento(false));
  }, []);

  const opzioniCapienza = useMemo(() => {
    const massima = camere.reduce(
      (max, camera) => Math.max(max, camera.capienzaMassima ?? 0),
      0,
    );
    return Array.from({ length: massima }, (_, indice) => indice + 1);
  }, [camere]);

  const camereFiltrate =
    capienzaMinima === ''
      ? camere
      : camere.filter(
          (camera) => camera.capienzaMassima >= Number(capienzaMinima),
        );

  if (caricamento) {
    return <p>Caricamento camere...</p>;
  }

  if (errore) {
    return <p className="error-msg">{errore}</p>;
  }

  return (
    <>
      <div className="form-camera">
        <div className="form-group">
          <label htmlFor="capienza">Capienza minima</label>
          <select
            id="capienza"
            value={capienzaMinima}
            onChange={(event) => setCapienzaMinima(event.target.value)}
          >
            <option value="">Tutte le capienze</option>
            {opzioniCapienza.map((capienza) => (
              <option key={capienza} value={capienza}>
                {capienza} ospiti
              </option>
            ))}
          </select>
        </div>
      </div>

      <p>Numero Camere: {camereFiltrate.length}</p>

      {camereFiltrate.length === 0 && (
        <p>Nessuna camera con questa capienza.</p>
      )}

      <div className="camere-grid">
        {camereFiltrate.map((camera) => (
          <div key={camera.id} className="camera-card">
            <h2>Camera {camera.numeroCamera}</h2>
            <ul className="camera-info">
              <li>
                <strong>Tipologia:</strong> {camera.tipologia}
              </li>
              <li>
                <strong>Capienza:</strong> {camera.capienzaMassima} ospiti
              </li>
              <li>
                <strong>Superficie:</strong> {camera.superficieMq ?? 'n/d'} mq
              </li>
              <li>
                <strong>Prezzo:</strong> {camera.prezzoBaseNotte} €/notte
              </li>
              <li>
                <strong>Stato:</strong> {camera.statoAttuale}
              </li>
            </ul>
            <a href={`/camere/${camera.id}`} className="btn">
              Dettaglio
            </a>
          </div>
        ))}
      </div>
    </>
  );
}

const container = document.getElementById('camere-widget');

if (container) {
  createRoot(container).render(<CamereWidget />);
}
