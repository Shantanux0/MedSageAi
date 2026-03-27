from fpdf import FPDF, XPos, YPos
from datetime import date

class MedicalReport(FPDF):
    def header(self):
        self.set_fill_color(30, 80, 120)
        self.rect(0, 0, 210, 30, 'F')
        self.set_text_color(255, 255, 255)
        self.set_font("Helvetica", "B", 18)
        self.set_xy(10, 8)
        self.cell(0, 10, "CITY GENERAL HOSPITAL",
                  new_x=XPos.LMARGIN, new_y=YPos.NEXT)
        self.set_font("Helvetica", "", 9)
        self.set_xy(10, 18)
        self.cell(0, 6,
                  "123 Medical Lane, Mumbai - 400001  |  Tel: +91-22-1234-5678  |  www.citygeneralhospital.in")
        self.set_text_color(0, 0, 0)

    def footer(self):
        self.set_y(-15)
        self.set_font("Helvetica", "I", 8)
        self.set_text_color(120, 120, 120)
        self.cell(0, 10,
                  f"Page {self.page_no()}  |  Confidential Medical Record  |  Generated: {date.today()}",
                  align="C")

def section(pdf, title):
    pdf.set_font("Helvetica", "B", 11)
    pdf.set_fill_color(220, 235, 255)
    pdf.set_text_color(0, 0, 0)
    pdf.cell(190, 8, f"  {title}", fill=True,
             new_x=XPos.LMARGIN, new_y=YPos.NEXT)
    pdf.ln(2)

def row(pdf, left_label, left_val, right_label, right_val):
    pdf.set_font("Helvetica", "B", 9)
    pdf.cell(35, 7, left_label + ":", new_x=XPos.RIGHT, new_y=YPos.TOP)
    pdf.set_font("Helvetica", "", 9)
    pdf.cell(60, 7, left_val, new_x=XPos.RIGHT, new_y=YPos.TOP)
    pdf.set_font("Helvetica", "B", 9)
    pdf.cell(35, 7, right_label + ":", new_x=XPos.RIGHT, new_y=YPos.TOP)
    pdf.set_font("Helvetica", "", 9)
    pdf.cell(60, 7, right_val, new_x=XPos.LMARGIN, new_y=YPos.NEXT)

pdf = MedicalReport()
pdf.add_page()
pdf.set_auto_page_break(auto=True, margin=20)

# Title
pdf.set_font("Helvetica", "B", 14)
pdf.set_fill_color(240, 245, 255)
pdf.set_xy(10, 35)
pdf.cell(190, 10, "COMPLETE BLOOD COUNT (CBC) REPORT",
         align="C", fill=True, new_x=XPos.LMARGIN, new_y=YPos.NEXT)
pdf.ln(4)

# Patient Info
section(pdf, "Patient Information")
row(pdf, "Patient Name",    "John Patient",          "Patient ID",   "MED-2025-00491")
row(pdf, "Date of Birth",   "15-Jun-1995",           "Blood Group",  "O Positive")
row(pdf, "Gender",          "Male",                  "Age",          "29 years")
row(pdf, "Referring Doctor","Dr. Sarah Smith",        "Speciality",   "Cardiology")
row(pdf, "Report Date",     "15-Jan-2025",           "Sample Type",  "Venous Blood")
row(pdf, "Collection Time", "08:30 AM",              "Report Time",  "11:00 AM")
pdf.ln(4)

# Results Table
section(pdf, "Investigation Results")
headers = ["Test Parameter", "Result", "Unit", "Normal Range", "Status"]
col_w   = [70, 25, 25, 45, 25]

pdf.set_font("Helvetica", "B", 9)
pdf.set_fill_color(50, 100, 150)
pdf.set_text_color(255, 255, 255)
for h, w in zip(headers, col_w):
    pdf.cell(w, 8, h, border=1, fill=True, align="C",
             new_x=XPos.RIGHT, new_y=YPos.TOP)
pdf.ln()

results = [
    ("Hemoglobin",        "13.2",  "g/dL",     "13.0 - 17.0",   "NORMAL"),
    ("Total WBC Count",   "11500", "cells/uL",  "4000 - 11000",  "ANOMALY"),
    ("RBC Count",         "4.8",   "mill/uL",   "4.5 - 5.5",     "NORMAL"),
    ("Hematocrit (PCV)",  "42",    "%",          "40 - 50",       "NORMAL"),
    ("MCV",               "87",    "fL",         "80 - 100",      "NORMAL"),
    ("MCH",               "28",    "pg",         "27 - 33",       "NORMAL"),
    ("MCHC",              "33",    "g/dL",       "32 - 36",       "NORMAL"),
    ("Platelet Count",    "145000","cells/uL",   "150000-400000", "ANOMALY"),
    ("Neutrophils",       "72",    "%",          "40 - 70",       "ANOMALY"),
    ("Lymphocytes",       "20",    "%",          "20 - 40",       "NORMAL"),
    ("Monocytes",         "6",     "%",          "2 - 8",         "NORMAL"),
    ("Eosinophils",       "2",     "%",          "1 - 4",         "NORMAL"),
    ("Fasting Glucose",   "110",   "mg/dL",      "70 - 100",      "ANOMALY"),
    ("Total Cholesterol", "195",   "mg/dL",      "< 200",         "NORMAL"),
    ("Blood Pressure",    "128/84","mmHg",       "< 120/80",      "ANOMALY"),
]

pdf.set_text_color(0, 0, 0)
for i, data_row in enumerate(results):
    fill = i % 2 == 0
    pdf.set_fill_color(248, 250, 255) if fill else pdf.set_fill_color(255, 255, 255)
    status = data_row[4]
    for j, (cell_val, w) in enumerate(zip(data_row, col_w)):
        is_last = (j == len(col_w) - 1)
        nx = XPos.LMARGIN if is_last else XPos.RIGHT
        ny = YPos.NEXT    if is_last else YPos.TOP
        if j == 4:
            pdf.set_text_color(200, 30, 30) if status == "ANOMALY" else pdf.set_text_color(30, 140, 30)
            pdf.set_font("Helvetica", "B" if status == "ANOMALY" else "", 9)
        else:
            pdf.set_text_color(0, 0, 0)
            pdf.set_font("Helvetica", "", 9)
        align = "L" if j == 0 else "C"
        pdf.cell(w, 7, cell_val, border=1, fill=fill, align=align, new_x=nx, new_y=ny)

pdf.set_text_color(0, 0, 0)
pdf.ln(5)

# Diagnoses
section(pdf, "Clinical Observations & Diagnosis")
pdf.set_font("Helvetica", "", 10)
observations = [
    "1. Elevated WBC count (leukocytosis) - possible infection or inflammatory response.",
    "2. Platelet count slightly below normal range - monitor for thrombocytopenia.",
    "3. Fasting glucose at 110 mg/dL - borderline pre-diabetic; dietary counselling advised.",
    "4. Blood pressure mildly elevated - lifestyle modification recommended.",
    "5. Neutrophilia noted - consistent with potential bacterial infection.",
]
for obs in observations:
    pdf.cell(190, 7, obs, new_x=XPos.LMARGIN, new_y=YPos.NEXT)
pdf.ln(4)

# Medications
section(pdf, "Prescribed Medications")
meds = [
    ("Metformin 500mg",   "Twice daily after meals", "30 days", "Pre-diabetic glucose control"),
    ("Amoxicillin 500mg", "Three times daily",        "7 days",  "Suspected bacterial infection"),
    ("Amlodipine 5mg",    "Once daily (morning)",     "30 days", "Mild hypertension management"),
]
med_cols = ["Medication", "Frequency", "Duration", "Purpose"]
med_w    = [55, 50, 30, 55]

pdf.set_font("Helvetica", "B", 9)
pdf.set_fill_color(50, 100, 150)
pdf.set_text_color(255, 255, 255)
for h, w in zip(med_cols, med_w):
    pdf.cell(w, 8, h, border=1, fill=True, align="C",
             new_x=XPos.RIGHT, new_y=YPos.TOP)
pdf.ln()

pdf.set_text_color(0, 0, 0)
for i, (med, freq, dur, purpose) in enumerate(meds):
    fill = i % 2 == 0
    pdf.set_fill_color(248, 250, 255) if fill else pdf.set_fill_color(255, 255, 255)
    pdf.set_font("Helvetica", "", 9)
    for j, (val, w) in enumerate(zip([med, freq, dur, purpose], med_w)):
        is_last = (j == len(med_w) - 1)
        nx = XPos.LMARGIN if is_last else XPos.RIGHT
        ny = YPos.NEXT    if is_last else YPos.TOP
        pdf.cell(w, 7, val, border=1, fill=fill, new_x=nx, new_y=ny)

pdf.ln(8)

# Signatures
pdf.set_text_color(0, 0, 0)
pdf.set_font("Helvetica", "B", 10)
pdf.cell(95, 7, "Pathologist Signature:", new_x=XPos.RIGHT, new_y=YPos.TOP)
pdf.cell(95, 7, "Referring Doctor Signature:", new_x=XPos.LMARGIN, new_y=YPos.NEXT)
pdf.ln(8)
pdf.set_font("Helvetica", "B", 10)
pdf.cell(95, 6, "Dr. Rajesh Kumar, MD Pathology", new_x=XPos.RIGHT, new_y=YPos.TOP)
pdf.cell(95, 6, "Dr. Sarah Smith, MD Cardiology", new_x=XPos.LMARGIN, new_y=YPos.NEXT)
pdf.set_font("Helvetica", "", 8)
pdf.set_text_color(100, 100, 100)
pdf.cell(95, 5, "Reg. No: MCI-98765 | City General Hospital", new_x=XPos.RIGHT, new_y=YPos.TOP)
pdf.cell(95, 5, "Reg. No: MCI-54321 | City General Hospital", new_x=XPos.LMARGIN, new_y=YPos.NEXT)

output_path = "/Users/shantanukale/Downloads/mediai/sample_blood_report.pdf"
pdf.output(output_path)
print(f"PDF generated: {output_path}")
