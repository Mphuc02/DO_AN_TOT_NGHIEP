import {forwardRef, useEffect, useRef, useState} from "react";
import {SendApiService} from "../../../service/SendApiService";
import {ExaminationResult, MEDICINE} from "../../../ApiConstant";
import styles from "../../../layouts/body/style.module.css";
import html2canvas from "html2canvas";
import {jsPDF} from "jspdf";

const ChooseMedicineModal = ({isOpen, onClose, onChooseMedicine}) => {
    const [valueSearch, setValueSearch] = useState('')
    const [medicinesMap, setMedicinesMap] = useState(new Map())

    const searchMedicine = () => {
        SendApiService.getRequest(MEDICINE.Medicine.search(valueSearch), {}, response => {
            const tempMap = new Map()
            for(const medicine of response.data){
                tempMap.set(medicine.id, medicine)
            }
            setMedicinesMap(tempMap)
        }, error => {

        })
    }

    useEffect(() => {
        searchMedicine()
    }, [valueSearch])

    if(!isOpen){
        return null
    }

    return (
            <div className={styles.modalOverlay}>
                <div className={styles.modalContent}>
                    <button className={styles.closeButton} onClick={onClose}>&times;</button>

                    <label>Tìm kiếm thuốc:</label>
                    <input onChange={(e) => setValueSearch(e.target.value)}/>

                    <table border={1}>
                        <thead>
                        <tr>
                            <td>Số thứ tự</td>
                            <td>Tên thuốc</td>
                            <td>Mô tả</td>
                            <td>Nguồn gốc</td>
                        </tr>
                        </thead>
                        <tbody>
                        {[...medicinesMap].map(([key, value], index) => {

                            return <tr key={value.id}>
                                <td>{index + 1}</td>
                                <td>{value.name}</td>
                                <td>{value.description}</td>
                                <td>{value.origin.name}</td>
                                <td><button onClick={() => onChooseMedicine(value)}>Chọn thuốc này</button></td>
                            </tr>
                        })}
                        </tbody>
                    </table>
                </div>
            </div>)
}

const PrintableComponent = forwardRef((props, ref) => {
    return (
        <div className={`${styles.printableContent}`} ref={ref}>
            <h2>Bệnh viện da liễu Minh Phúc</h2>
            <p>Phiếu tư vấn sử dụng thuốc</p>
            <p>Bác sĩ: {}</p>
            <p>Thời gian tạo: </p>
            <p>Danh sách thuốc tư vấn</p>

            <table border={1}>
                <thead>
                    <tr>
                        <td>Số thứ tự</td>
                        <td>Tên thuốc</td>
                        <td>Cách dùng</td>
                        <td>Số lượng tư vấn</td>
                    </tr>
                </thead>
            </table>
        </div>
    );
});

const MedicineConsultation = ({}) => {
    const [isOpenChooseMedicineModal, setIsOpenChooseMedicineModal] = useState(false)
    const [selectedMedicinesMap, setSelectedMedicinesMap] = useState(new Map())
    const [formError, setFormError] = useState({})
    const [formResponse, setFormResponse] = useState(null)
    let isClickCreateConsultation = false;
    const printRef = useRef();
    const id = window.location.href.split('/').pop()

    const onCloseChooseMedicineModal = () => {
        setIsOpenChooseMedicineModal(false)
    }

    const openChooseMedicineModal = () => {
        setIsOpenChooseMedicineModal(true)
    }

    const onChooseMedicine = (medicine) => {
        setIsOpenChooseMedicineModal(false)
        selectedMedicinesMap.set(medicine.id, medicine)
        setSelectedMedicinesMap(new Map(selectedMedicinesMap))
    }

    const onRemoveMedicine = (id) => {
        selectedMedicinesMap.delete(id)
        setSelectedMedicinesMap(new Map(selectedMedicinesMap))
    }

    const onChangeUsageMedicine = (id, value) => {
        const medicine = selectedMedicinesMap.get(id)
        medicine.treatment = value
        setSelectedMedicinesMap(new Map(selectedMedicinesMap))
    }

    const onChangeQuantity = (id, quantity) => {
        const medicine = selectedMedicinesMap.get(id)
        medicine.consultedQuantity = quantity
        setSelectedMedicinesMap(new Map(selectedMedicinesMap))
    }

    const onClickCreateConsultationForm = () => {
        if(isClickCreateConsultation){
            return
        }

        isClickCreateConsultation = true
        const details = [...selectedMedicinesMap].map(([key, value]) => {
            return {
                medicineId: key,
                quantity: value.consultedQuantity,
                treatment: value.treatment
            }
        })

        const consultationForm = {
            id: id,
            details: details
        }

        SendApiService.postRequest(ExaminationResult.MedicineConsultationForm.getUrl(), consultationForm, {}, response => {
            setFormResponse(response.data)
            setFormError({})
            isClickCreateConsultation = false
            alert('Tạo phiếu tư vấn thành công')
        }, error => {
            if(error.status === 400){
                alert('Lỗi: ' + error.response.data.message)
                setFormError(error.response.data.fields)
            }
            isClickCreateConsultation = false
        })
    }

    const handleDownloadPdf = async () => {
        const element = printRef.current;

        // Điều chỉnh kích thước canvas để giữ đúng cỡ chữ
        const canvas = await html2canvas(element, {
            scale: 3, // Giảm scale nếu chữ quá to để đảm bảo tỷ lệ phù hợp
        });

        const imgData = canvas.toDataURL('image/png');
        const pdf = new jsPDF({
            orientation: 'portrait',
            unit: 'pt',
            format: 'a4',
        });

        // Điều chỉnh kích thước hình ảnh để phù hợp với PDF
        const pageWidth = pdf.internal.pageSize.getWidth();
        const imgWidth = pageWidth;
        const imgHeight = (canvas.height * imgWidth) / canvas.width;

        pdf.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight);
        pdf.output('dataurlnewwindow');
    };

    return (
        <div>
            <button onClick={() => openChooseMedicineModal()}>Chọn thuốc</button>
            <ChooseMedicineModal isOpen={isOpenChooseMedicineModal} onClose={onCloseChooseMedicineModal}
                                 onChooseMedicine={onChooseMedicine}/>

            <table>
                <thead>
                <tr>
                    <td>Số thứ tự</td>
                    <td>Tên thuốc</td>
                    <td>Số lượng trong kho</td>
                    <td>Cách dùng</td>
                    <td>Số lượng tư vấn</td>
                </tr>
                </thead>
                <tbody>
                {[...selectedMedicinesMap].map(([key, value], index) => {
                    const detailAtIndex = `details[${index}].`
                    console.log(detailAtIndex + 'treatment', formError)

                    return <>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>{formError[detailAtIndex + 'treatment']}</td>
                            <td>{formError[detailAtIndex + 'quantity']}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>{index + 1}</td>
                            <td>{value.name}</td>
                            <td>{value.quantity}</td>
                            <td><input value={value.treatment}
                                       onChange={(e) => onChangeUsageMedicine(key, e.target.value)}/></td>
                            <td><input type={"number"} onChange={(e) => onChangeQuantity(key, e.target.value)}/></td>
                            <td>
                                <button onClick={() => onRemoveMedicine(key)}>Xóa</button>
                            </td>
                        </tr>
                    </>
                })}
                </tbody>
            </table>

            <PrintableComponent ref={printRef}/>
            {!formResponse && <button onClick={() => {
                onClickCreateConsultationForm()
            }}>Tạo phiếu tư vấn thuốc</button>}
            {formResponse && <button onClick={() => handleDownloadPdf()}>In phiếu tư vấn</button>}
        </div>
    )
}

export {MedicineConsultation}