# MediSage AI — Intelligence Behind Every Diagnosis 🧬✨

**MediSage AI** is a premium medical intelligence platform designed to transform unstructured clinical data into actionable health insights. Using state-of-the-art Large Language Models (LLMs), it seamlessly parses complex PDF lab reports (CBC, Lipid Profiles, etc.) and provides a cinematic, intuitive dashboard for patients and healthcare providers.

---

## 🚀 Vision
Medical reports are the heartbeat of diagnostics, yet they often remain trapped in static documents. MediSage AI liberates this data, offering:
*   **Instant Extraction:** No more manual entry. Upload a PDF, and the AI handles the rest.
*   **Clinical Intelligence:** Detects physiological anomalies and provides simple, human-readable summaries.
*   **Cinematic Experience:** High-performance, low-latency UI designed for modern medical institutions.

---

## ✨ Features
*   **🤖 AI Data Extraction:** Advanced parsing of hematology, lipid, and diabetic profiles using the Groq/LLama-3.1 engine.
*   **📊 Dynamic Vitals Tracking:** Interactive, color-coded health tracking with automatic anomaly detection.
*   **💬 Context-Aware AI Chat:** A personal clinical assistant that answers questions based strictly on your historical records.
*   **🖥 Multi-Portal Dashboard:** Specialized, integrated views for **Patients**, **Doctors**, and **Administrators**.
*   **🎭 Typewriter Interaction:** Fluid character-streaming AI responses for a more natural, conversational experience.
*   **🔒 Bank-Grade Security:** JWT-authenticated sessions and secure document serving.

---

## 🛠 Tech Stack
*   **Frontend:** React 18, Tailwind CSS, Framer Motion (Animations), Recharts (Analytics), Lucide-React.
*   **Backend:** Java 17, Spring Boot 3.2, Spring Security (JWT), PostgreSQL.
*   **AI Engine:** Groq Cloud API (Llama-3.1-8b-instant) for real-time medical synthesis.

---

## ⚙️ Project Setup

### Prerequisites
*   Java 17+ & Maven
*   Node.js 18+ & NPM
*   PostgreSQL 14+
*   [Groq API Key](https://console.groq.com/keys) (Free)

### 1. Backend Configuration
1.  Open `src/main/resources/application.properties`.
2.  Set your database credentials:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/medi_sage_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```
3.  Add your Groq API Key:
    ```properties
    groq.api.key=YOUR_GROQ_API_KEY
    ```
4.  Run from the root directory:
    ```bash
    mvn spring-boot:run
    ```

### 2. Frontend Configuration
1.  Navigate to the `/frontend` directory.
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Start the development server:
    ```bash
    npm run dev
    ```

---

## 📸 Screenshots & Demo
*   **Cinematic Landing:** Explore our high-contrast "Dark Emerald" medical aesthetic.
*   **AI Summary:** Experience the typewriter-streaming clinical insights.
*   **Vitals Tracker:** View dynamic, color-coded health trending.

---

## 🤝 Contribution
Contributions are welcome! Please fork this repository and submit periodic pull requests for clinical feature enhancements or UI refinements.

---

## ⚖️ License
Distributed under the MIT License. See `LICENSE` for more information.

---
**MediSage AI** — *The future of clinical intelligence.*
