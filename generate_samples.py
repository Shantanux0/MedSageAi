from fpdf import FPDF, XPos, YPos
from datetime import date, timedelta

class MedicalReport(FPDF):
    def header(self):
        self.set_fill_color(0, 150, 136) # Teal/Emerald
        self.rect(0, 0, 210, 30, 'F')
        self.set_text_color(255, 255, 255)
        self.set_font("Helvetica", "B", 18)
        self.set_xy(10, 8)
        self.cell(0, 10, "MEDAI CLINICAL NETWORK", new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.set_font("Helvetica", "", 9)
        self.set_xy(10, 18)
        self.cell(0, 6, "Digital Health Infrastructure | www.medai.io | Confidential Patient Record")
        self.set_text_color(0, 0, 0)

    def footer(self):
        self.set_y(-15)
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(120, 120, 120)
        self.cell(0, 10, f"Page {self.page_no()} | MedAI Verified Record | System Generated: {date.today()}", align="C")

def create_report(filename, title, patient, date_str, results, observations, meds):
    pdf = MedicalReport()
    pdf.add_page()
    pdf.set_auto_page_break(auto=True, margin=20)
    
    # Title
    pdf.set_font("Helvetica", "B", 14)
    pdf.set_fill_color(240, 250, 248)
    pdf.set_xy(10, 35)
    pdf.cell(190, 10, title.upper(), align="C", fill=True, new_x=XPos.LMARGIN, new_y=YPos.NEXT)
    pdf.ln(5)

    # Info
    pdf.set_font("Helvetica", "B", 10)
    pdf.cell(40, 7, "Patient Name:", new_x=XPos.RIGHT)
    pdf.set_font("Helvetica", "", 10)
    pdf.cell(60, 7, patient, new_x=XPos.RIGHT)
    pdf.set_font("Helvetica", "B", 10)
    pdf.cell(40, 7, "Report Date:", new_x=XPos.RIGHT)
    pdf.set_font("Helvetica", "", 10)
    pdf.cell(50, 7, date_str, new_x=XPos.LMARGIN, new_y=YPos.NEXT)
    pdf.ln(5)

    # Results
    pdf.set_font("Helvetica", "B", 11)
    pdf.set_fill_color(230, 230, 230)
    pdf.cell(190, 8, "  INVESTIGATION FINDINGS", fill=True, new_x=XPos.LMARGIN, new_y=YPos.NEXT)
    pdf.ln(2)
    pdf.set_font("Helvetica", "", 10)
    for r in results:
        pdf.multi_cell(190, 7, f" - {r}", new_x=XPos.LMARGIN, new_y=YPos.NEXT)
    pdf.ln(5)

    # Observations
    pdf.set_font("Helvetica", "B", 11)
    pdf.set_fill_color(230, 230, 230)
    pdf.cell(190, 8, "  CLINICAL OBSERVATIONS", fill=True, new_x=XPos.LMARGIN, new_y=YPos.NEXT)
    pdf.ln(2)
    pdf.set_font("Helvetica", "", 10)
    for o in observations:
        pdf.multi_cell(190, 7, f" - {o}", new_x=XPos.LMARGIN, new_y=YPos.NEXT)
    pdf.ln(5)

    # Meds
    if meds:
        pdf.set_font("Helvetica", "B", 11)
        pdf.set_fill_color(230, 230, 230)
        pdf.cell(190, 8, "  PRESCRIBED PLAN", fill=True, new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        pdf.ln(2)
        pdf.set_font("Helvetica", "", 10)
        for m in meds:
            pdf.multi_cell(190, 7, f" - {m}", new_x=XPos.LMARGIN, new_y=YPos.NEXT)

    pdf.output(filename)
    print(f"Generated: {filename}")

# 1. Blood Test (Late 2023 - CRITICAL)
create_report(
    "blood_test_dec2023.pdf",
    "Complete Metabolic Panel",
    "John Patient",
    "12-Dec-2023",
    ["Glucose: 420 mg/dL (CRITICAL HIGH)", "Hemoglobin: 6.2 g/dL (CRITICAL LOW)", "Potassium: 6.8 mEq/L (CRITICAL HIGH)"],
    ["PATIENT ALERT: Extreme hyperglycemia noted.", "Severely low hemoglobin - risk of acute anemia.", "Critical Hyperkalemia - requires immediate cardiac monitoring."],
    ["IMMEDIATE Hospital Admission recommended", "Insulin drip protocol", "Calcium gluconate for cardiac protection"]
)

# 2. MRI Brain (Feb 2024 - CRITICAL)
create_report(
    "mri_brain_feb2024.pdf",
    "Magnetic Resonance Imaging - Brain",
    "John Patient",
    "24-Feb-2024",
    ["Midline shift: 8mm to the left.", "Acute intracranial hemorrhage in the right parietal lobe.", "Evidence of early brainstem herniation."],
    ["CRITICAL: Large acute hematoma (4.5cm) noted.", "Significant mass effect on the lateral ventricles.", "Immediate neurosurgical consultation required."],
    ["Stat Neurosurgery referral", "Mannitol for intracranial pressure control", "Keep patient NPO for emergency surgery"]
)

# 3. Chest X-Ray (Mar 2024 - CRITICAL)
create_report(
    "chest_xray_mar2024.pdf",
    "Radiology Report - Chest PA View",
    "John Patient",
    "15-Mar-2024",
    ["Tension Pneumothorax noted on the right side.", "Massive mediastinal shift to the left.", "Cardiac silhouette appears enlarged."],
    ["DEATH RISK: Complete collapse of the right lung.", "Tracheal deviation noted - life-threatening emergency.", "Prompt needle decompression required."],
    ["Emergency Chest Tube Insertion (Right side)", "High-flow Oxygen protocol", "Immediate transfer to ICU"]
)
