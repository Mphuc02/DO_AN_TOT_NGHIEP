import html2canvas from "html2canvas";
import {jsPDF} from "jspdf";

const PrintComponent = async (printRef) => {
    const replaceInputsWithSpans = (element) => {
        const inputs = element.querySelectorAll('input, textarea');
        inputs.forEach(input => {
            const span = document.createElement('span');
            span.textContent = input.value || input.placeholder;
            span.style.cssText = window.getComputedStyle(input).cssText;
            input.parentNode.replaceChild(span, input);
        });
    };

    const restoreInputs = (element, originalInputs) => {
        const spans = element.querySelectorAll('span');
        spans.forEach((span, index) => {
            const originalInput = originalInputs[index];
            span.parentNode.replaceChild(originalInput, span);
        });
    };

    const element = printRef.current;
    if (!element) {
        return;
    }

    const originalInputs = Array.from(element.querySelectorAll('input, textarea'));
    replaceInputsWithSpans(element);

    const canva = await html2canvas(element);
    restoreInputs(element, originalInputs);  // Khôi phục lại trạng thái ban đầu

    const data = canva.toDataURL('image/png');
    const doc = new jsPDF({
        orientation: "portrait",
        unit: "px",
        format: "a4"
    });

    const imageProperties = doc.getImageProperties(data);
    const width = doc.internal.pageSize.getWidth();
    const height = (imageProperties.height * width) / imageProperties.width;
    doc.addImage(data, 'PNG', 0, 0, width, height);

    const pdfBlob = doc.output('blob');
    const pdfUrl = URL.createObjectURL(pdfBlob);
    window.open(pdfUrl, '_blank');
}

export {PrintComponent}