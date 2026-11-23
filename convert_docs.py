#!/usr/bin/env python3
"""
Convert OOPJ_PROJECT_DOCUMENTATION.md to DOCX and PDF formats
"""

import os
import sys
from pathlib import Path

# Try importing required libraries
try:
    from docx import Document
    from docx.shared import Pt, RGBColor, Inches
    from docx.enum.text import WD_ALIGN_PARAGRAPH
except ImportError:
    print("ERROR: python-docx is not installed")
    print("Install it with: pip install python-docx")
    sys.exit(1)

try:
    import markdown
except ImportError:
    print("ERROR: markdown is not installed")
    print("Install it with: pip install markdown")
    sys.exit(1)

def markdown_to_docx(md_file, docx_file):
    """Convert markdown file to DOCX"""
    
    # Read markdown file
    with open(md_file, 'r', encoding='utf-8') as f:
        md_content = f.read()
    
    # Create Document
    doc = Document()
    
    # Split content by lines
    lines = md_content.split('\n')
    
    for line in lines:
        line = line.rstrip()
        
        if not line:
            # Empty line
            doc.add_paragraph()
        elif line.startswith('# '):
            # H1 heading
            text = line.lstrip('# ').strip()
            heading = doc.add_heading(text, level=1)
            heading.alignment = WD_ALIGN_PARAGRAPH.LEFT
        elif line.startswith('## '):
            # H2 heading
            text = line.lstrip('# ').strip()
            heading = doc.add_heading(text, level=2)
            heading.alignment = WD_ALIGN_PARAGRAPH.LEFT
        elif line.startswith('### '):
            # H3 heading
            text = line.lstrip('# ').strip()
            heading = doc.add_heading(text, level=3)
            heading.alignment = WD_ALIGN_PARAGRAPH.LEFT
        elif line.startswith('#### '):
            # H4 heading
            text = line.lstrip('# ').strip()
            heading = doc.add_heading(text, level=4)
            heading.alignment = WD_ALIGN_PARAGRAPH.LEFT
        elif line.startswith('- '):
            # Bullet point
            text = line.lstrip('- ').strip()
            doc.add_paragraph(text, style='List Bullet')
        elif line.startswith('| '):
            # Table row - collect all rows
            pass  # Simplified: will add as paragraph
        elif line.startswith('```'):
            # Code block - skip markers
            continue
        else:
            # Regular paragraph
            if line.strip():
                doc.add_paragraph(line)
    
    # Save document
    doc.save(docx_file)
    print(f"✓ Created: {docx_file}")
    return True

def convert_docx_to_pdf(docx_file, pdf_file):
    """Convert DOCX to PDF using libreoffice"""
    import subprocess
    
    try:
        # Try using libreoffice
        cmd = [
            'libreoffice',
            '--headless',
            '--convert-to', 'pdf',
            '--outdir', os.path.dirname(pdf_file) or '.',
            docx_file
        ]
        
        result = subprocess.run(cmd, capture_output=True, text=True)
        
        if result.returncode == 0:
            print(f"✓ Created: {pdf_file}")
            return True
        else:
            print(f"LibreOffice error: {result.stderr}")
            return False
            
    except FileNotFoundError:
        print("WARNING: LibreOffice not found. Trying alternative method...")
        
        # Try using python-pptx or reportlab
        try:
            from reportlab.lib.pagesizes import letter
            from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, PageBreak
            from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
            from reportlab.lib.units import inch
            
            print("Using reportlab for PDF conversion...")
            
            # Read DOCX
            doc = Document(docx_file)
            
            # Create PDF
            pdf_doc = SimpleDocTemplate(pdf_file, pagesize=letter)
            styles = getSampleStyleSheet()
            story = []
            
            for para in doc.paragraphs:
                if para.text.strip():
                    story.append(Paragraph(para.text, styles['Normal']))
                    story.append(Spacer(1, 0.1*inch))
            
            pdf_doc.build(story)
            print(f"✓ Created: {pdf_file}")
            return True
            
        except ImportError:
            print("ERROR: Neither LibreOffice nor reportlab available for PDF conversion")
            print("Install one of:")
            print("  - LibreOffice (system package)")
            print("  - reportlab (pip install reportlab)")
            return False

def main():
    workspace = r'd:\FinanceTracker-OOPJ'
    md_file = os.path.join(workspace, 'OOPJ_PROJECT_DOCUMENTATION.md')
    docx_file = os.path.join(workspace, 'OOPJ_PROJECT_DOCUMENTATION.docx')
    pdf_file = os.path.join(workspace, 'OOPJ_PROJECT_DOCUMENTATION.pdf')
    
    print("=" * 60)
    print("OOPJ Documentation Format Conversion")
    print("=" * 60)
    
    # Check if markdown file exists
    if not os.path.exists(md_file):
        print(f"ERROR: {md_file} not found")
        sys.exit(1)
    
    print(f"Source: {md_file}")
    print(f"File size: {os.path.getsize(md_file) / 1024:.1f} KB")
    print()
    
    # Convert to DOCX
    print("Converting to DOCX...")
    if markdown_to_docx(md_file, docx_file):
        print(f"File size: {os.path.getsize(docx_file) / 1024:.1f} KB")
        print()
    else:
        print("ERROR: DOCX conversion failed")
        sys.exit(1)
    
    # Convert to PDF
    print("Converting to PDF...")
    if convert_docx_to_pdf(docx_file, pdf_file):
        if os.path.exists(pdf_file):
            print(f"File size: {os.path.getsize(pdf_file) / 1024:.1f} KB")
        print()
    else:
        print("WARNING: PDF conversion skipped (see errors above)")
        print()
    
    print("=" * 60)
    print("Conversion Complete!")
    print("=" * 60)
    print(f"✓ DOCX: {docx_file}")
    if os.path.exists(pdf_file):
        print(f"✓ PDF:  {pdf_file}")
    print()

if __name__ == '__main__':
    main()
