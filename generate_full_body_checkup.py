from fpdf import FPDF, XPos, YPos
from datetime import date

class MedicalReport(FPDF):
    def header(self):
        # Header background
        self.set_fill_color(0, 44, 27) # MediSage Green
        self.rect(0, 0, 210, 35, 'F')
        
        # Logo placeholder or text
        self.set_text_color(255, 255, 255)
        self.set_font("Helvetica", "B", 18)
        self.set_xy(15, 10)
        self.cell(0, 10, "MEDISAGE CLINICAL LABORATORIES", new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        
        self.set_font("Helvetica", "", 10)
        self.set_xy(15, 20)
        self.cell(0, 5, "Advanced Diagnostic Center | ISO 9001:2015 Certified", new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.set_xy(15, 25)
        self.cell(0, 5, "77 Science Park Drive, Innovation Hub, Mumbai | +91 22 9876 5432", new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.ln(10)

    def footer(self):
        self.set_y(-20)
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(128, 128, 128)
        self.cell(0, 10, f"Page {self.page_no()} | System ID: MS-AI-{date.today().strftime('%Y%j')} | Confidential Document", align="C")

def draw_section_header(pdf, title):
    pdf.ln(5)
    pdf.set_font("Helvetica", "B", 12)
    pdf.set_fill_color(240, 248, 245)
    pdf.set_text_color(0, 44, 27)
    pdf.cell(180, 10, f"  {title}", fill=True, border='L', new_x=XPos.LMARGIN, new_y=YPos.NEXT)
    pdf.ln(2)

def draw_result_row(pdf, param, result, unit, normal_range, is_anomaly=False):
    pdf.set_font("Helvetica", "", 10)
    pdf.set_text_color(0, 0, 0)
    
    # Check for anomaly color
    if is_anomaly:
        pdf.set_text_color(200, 40, 40) # Red
        pdf.set_font("Helvetica", "B", 10)
    
    pdf.cell(70, 8, param, border='B')
    pdf.cell(30, 8, result, border='B', align='C')
    pdf.cell(30, 8, unit, border='B', align='C')
    pdf.cell(50, 8, normal_range, border='B', align='C')
    pdf.ln(8)
    pdf.set_text_color(0, 0, 0)

pdf = MedicalReport()
pdf.add_page()
pdf.set_auto_page_break(auto=True, margin=25)

# Report Title
pdf.ln(25)
pdf.set_font("Helvetica", "B", 16)
pdf.set_text_color(0, 44, 27)
pdf.cell(180, 10, "ANNUAL EXECUTIVE HEALTH AUDIT", align='C', new_x=XPos.LMARGIN, new_y=YPos.NEXT)
pdf.set_font("Helvetica", "", 10)
pdf.cell(180, 5, f"Report Generated: {date.today().strftime('%d %b %Y')}", align='C', new_x=XPos.LMARGIN, new_y=YPos.NEXT)
pdf.ln(8)

# Patient Data
pdf.set_fill_color(250, 250, 250)
pdf.rect(15, 75, 180, 35, 'F')
pdf.set_xy(20, 78)
pdf.set_font("Helvetica", "B", 10)
pdf.cell(30, 6, "Patient Name:")
pdf.set_font("Helvetica", "", 10)
pdf.cell(60, 6, "James Archer")
pdf.set_font("Helvetica", "B", 10)
pdf.cell(30, 6, "Patient ID:")
pdf.set_font("Helvetica", "", 10)
pdf.cell(60, 6, "PID-882910")
pdf.ln(7)

pdf.set_font("Helvetica", "B", 10)
pdf.set_x(20)
pdf.cell(30, 6, "Age / Gender:")
pdf.set_font("Helvetica", "", 10)
pdf.cell(60, 6, "52 Years / Male")
pdf.set_font("Helvetica", "B", 10)
pdf.cell(30, 6, "Report Date:")
pdf.set_font("Helvetica", "", 10)
pdf.cell(60, 6, date.today().strftime('%d-%m-%Y'))
pdf.ln(7)

pdf.set_font("Helvetica", "B", 10)
pdf.set_x(20)
pdf.cell(30, 6, "Ref. Doctor:")
pdf.set_font("Helvetica", "", 10)
pdf.cell(60, 6, "Dr. Elena Vance")
pdf.set_font("Helvetica", "B", 10)
pdf.cell(30, 6, "Contact:")
pdf.set_font("Helvetica", "", 10)
pdf.cell(60, 6, "+91 99001 10099")
pdf.ln(15)

# --- SECTION 1: HEMATOLOGY ---
draw_section_header(pdf, "HEMATOLOGY - COMPLETE BLOOD COUNT")
pdf.set_font("Helvetica", "B", 9)
pdf.set_fill_color(240, 240, 240)
pdf.cell(70, 8, " Parameter", fill=True)
pdf.cell(30, 8, "Result", fill=True, align='C')
pdf.cell(30, 8, "Unit", fill=True, align='C')
pdf.cell(50, 8, "Ref. Range", fill=True, align='C')
pdf.ln(8)

draw_result_row(pdf, "Hemoglobin (Hb)", "10.8", "g/dL", "13.0 - 17.0", is_anomaly=True)
draw_result_row(pdf, "RBC Count", "4.2", "mill/uL", "4.5 - 5.5", is_anomaly=True)
draw_result_row(pdf, "HCT", "34.5", "%", "40.0 - 50.0", is_anomaly=True)
draw_result_row(pdf, "MCV", "82.1", "fL", "80 - 100")
draw_result_row(pdf, "WBC Total Count", "12,400", "cells/cumm", "4,000 - 11,000", is_anomaly=True)
draw_result_row(pdf, "Platelet Count", "165,000", "cells/cumm", "150,000 - 450,000")

# --- SECTION 2: DIABETIC PROFILE ---
draw_section_header(pdf, "DIABETIC PROFILE")
draw_result_row(pdf, "Glucose - Fasting", "168", "mg/dL", "70 - 100", is_anomaly=True)
draw_result_row(pdf, "Glucose - Post Prandial", "245", "mg/dL", "100 - 140", is_anomaly=True)
draw_result_row(pdf, "HbA1c (Glycosylated Hb)", "8.2", "%", "4.0 - 5.6", is_anomaly=True)
draw_result_row(pdf, "Estimated Avg Glucose", "189", "mg/dL", "70 - 126", is_anomaly=True)

# --- SECTION 3: LIPID PROFILE ---
pdf.add_page()
pdf.ln(25)
draw_section_header(pdf, "LIPID PROFILE (CHOLESTEROL)")
draw_result_row(pdf, "Total Cholesterol", "248", "mg/dL", "< 200", is_anomaly=True)
draw_result_row(pdf, "Triglycerides", "210", "mg/dL", "< 150", is_anomaly=True)
draw_result_row(pdf, "HDL Cholesterol (Good)", "32", "mg/dL", "> 40", is_anomaly=True)
draw_result_row(pdf, "LDL Cholesterol (Bad)", "174", "mg/dL", "< 100", is_anomaly=True)
draw_result_row(pdf, "VLDL Cholesterol", "42", "mg/dL", "5 - 40", is_anomaly=True)

# --- SECTION 4: VITAL SIGNS ---
draw_section_header(pdf, "VITAL SIGNS & CLINICAL METRICS")
draw_result_row(pdf, "Blood Pressure (Systolic)", "155", "mmHg", "90 - 120", is_anomaly=True)
draw_result_row(pdf, "Blood Pressure (Diastolic)", "98", "mmHg", "60 - 80", is_anomaly=True)
draw_result_row(pdf, "Body Mass Index (BMI)", "31.4", "kg/m2", "18.5 - 24.9", is_anomaly=True)
draw_result_row(pdf, "Heart Rate (Resting)", "88", "bpm", "60 - 100")

# --- CLINICAL SUMMARY & PLAN ---
draw_section_header(pdf, "CLINICAL OBSERVATIONS & DIAGNOSIS")
pdf.set_font("Helvetica", "", 10)
pdf.multi_cell(180, 6, "1. PATIENT ALERT: Significant hyperglycemia noted with HbA1c of 8.2% and fasting glucose of 168 mg/dL, indicating uncontrolled Type 2 Diabetes Mellitus.\n2. Dyslipidemia: Elevated Total Cholesterol and LDL with critically low HDL levels. High risk of cardiovascular event.\n3. Hypertension: Stage 2 Hypertension (155/98 mmHg) observed in clinical setting.\n4. Microcytic Anemia: Hemoglobin at 10.8 g/dL suggests moderate iron-deficiency anemia or chronic disease impact.\n5. Leukocytosis: Elevated WBC count indicates an underlying low-grade infection or generalized inflammation.")

draw_section_header(pdf, "PRESCRIBED REMEDIATION PLAN")
pdf.set_font("Helvetica", "B", 10)
pdf.cell(100, 7, "Medication Name", border='B')
pdf.cell(80, 7, "Dosage & Frequency", border='B')
pdf.ln(8)
pdf.set_font("Helvetica", "", 10)
pdf.cell(100, 7, "Metformin Hydrochloride 1000mg", border='B')
pdf.cell(80, 7, "1 tab - Twice Daily (After Meals)", border='B')
pdf.ln(7)
pdf.cell(100, 7, "Amlodipine Besylate 5mg", border='B')
pdf.cell(80, 7, "1 tab - Once Daily (Morning)", border='B')
pdf.ln(7)
pdf.cell(100, 7, "Atorvastatin 20mg", border='B')
pdf.cell(80, 7, "1 tab - Bedtime", border='B')
pdf.ln(15)

# Signatures
pdf.set_font("Helvetica", "B", 11)
pdf.cell(90, 7, "Dr. Elena Vance, MD")
pdf.cell(90, 7, "Verified by AI Core", align='R')
pdf.ln(5)
pdf.set_font("Helvetica", "", 9)
pdf.cell(90, 7, "Senior Consultant Physician")
pdf.cell(90, 7, "MediSage Medical Intelligence", align='R')

output_path = "/Users/shantanukale/Downloads/mediai/full_body_checkup_report.pdf"
pdf.output(output_path)
print(f"COMPLEX REPORT GENERATED: {output_path}")
