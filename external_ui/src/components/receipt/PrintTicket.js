import React from 'react';
import { jsPDF } from 'jspdf';

const TicketPrintPage = () => {
    const ticketData = {
        hospitalName: 'Benh vien ABC',  // Viết không dấu để tránh lỗi
        patientName: 'Nguyen Van A',
        ticketNumber: '123',
        appointmentDate: '2024-11-01 10:00',
        roomNumber: 'Phong 101',
    };

    const createAndPrintPDF = () => {
        const doc = new jsPDF();

        // Nội dung PDF
        doc.setFontSize(20);
        doc.text(ticketData.hospitalName, 105, 20, { align: 'center' });

        doc.setFontSize(16);
        doc.text('PHIEU SO THU TU DOI KHAM', 105, 40, { align: 'center' });

        doc.setFontSize(30);
        doc.text(`So Thu Tu: ${ticketData.ticketNumber}`, 105, 70, { align: 'center' });

        doc.setFontSize(14);
        doc.text(`Ten Benh Nhan: ${ticketData.patientName}`, 20, 100);
        doc.text(`Ngay Hen: ${ticketData.appointmentDate}`, 20, 110);
        doc.text(`Phong Kham: ${ticketData.roomNumber}`, 20, 120);

        doc.setFontSize(12);
        doc.text('Xin vui long doi den khi so cua ban duoc goi de vao kham.', 105, 140, { align: 'center' });

        // Mở hộp thoại in
        doc.autoPrint();
        window.open(doc.output('bloburl'), '_blank');
    };

    return (
        <div style={{ textAlign: 'center', marginTop: '20px' }}>
            <h2>Phiếu Số Thứ Tự</h2>
            <button onClick={createAndPrintPDF}>In phiếu</button>
        </div>
    );
};

export default TicketPrintPage;
