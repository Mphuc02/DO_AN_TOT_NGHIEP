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

                    <table className="table-auto border-collapse border border-gray-300 w-full text-left"
                        border={1}>
                        <thead className="bg-gray-200">
                        <tr>
                            <td className="border border-gray-300 px-4 py-2">Số thứ tự</td>
                            <td className="border border-gray-300 px-4 py-2">Tên thuốc</td>
                            <td className="border border-gray-300 px-4 py-2">Mô tả</td>
                            <td className="border border-gray-300 px-4 py-2">Nguồn gốc</td>
                            <td className="border border-gray-300 px-4 py-2"></td>
                        </tr>
                        </thead>
                        <tbody>
                        {[...medicinesMap].map(([key, value], index) => {

                            return <tr key={value.id}>
                                <td>{index + 1}</td>
                                <td>{value.name}</td>
                                <td>{value.description}</td>
                                <td>{value.origin.name}</td>
                                <td><button
                                    className="bg-blue-500 text-white font-semibold px-4 py-2 rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-300 mt-5"
                                    onClick={() => onChooseMedicine(value)}>Chọn thuốc này</button></td>
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
        const element = printRef.current
        if(!element){
            return
        }
        const canva = await html2canvas(element)
        const data = canva.toDataURL('image/png')
        const doc = new jsPDF({
            orientation: "portrait",
            unit: "px",
            format: "a4"
        })

        const imageProperties = doc.getImageProperties(data)
        const width = doc.internal.pageSize.getWidth()

        const height = (imageProperties.height * width) / imageProperties.width
        doc.addImage(data, 'PNG', 0, 0, width, height)
        const pdfBlob = doc.output('blob');
        const pdfUrl = URL.createObjectURL(pdfBlob);
        window.open(pdfUrl, '_blank');
    };

    const getMedicine = (form) => {
        const medicineMap = new Map()
        for(const medicine of form.details){
            medicineMap.set(medicine.medicineId, medicine)
        }
        return new Promise((resolve, reject) => {
            SendApiService.postRequest(MEDICINE.Medicine.getByIDs(), [...medicineMap.keys()], {}, response => {
                for(const medicine of response.data){
                    medicineMap.get(medicine.id).medicine = medicine
                }
                setSelectedMedicinesMap(medicineMap)
            }, error => {

            })
        })
    }

    const getConsultationForm = () => {
        SendApiService.getRequest(ExaminationResult.MedicineConsultationForm.byId(id), {}, async response => {
            if(response.data){
                setFormResponse(response.data)
                await getMedicine(response.data)
            }
        }, error => {

        })
    }

    useEffect(() => {
        getConsultationForm()
    }, [])

    return (
        <div>
            {!formResponse && <button
                className="bg-blue-500 text-white font-semibold px-4 py-2 rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-300"
                onClick={() => openChooseMedicineModal()}>Chọn thuốc</button>}
            <ChooseMedicineModal isOpen={isOpenChooseMedicineModal} onClose={onCloseChooseMedicineModal}
                                 onChooseMedicine={onChooseMedicine}/>

            <p className="text-red-500 text-sm font-medium">{formError.details}</p>
            <table ref={printRef} className="table-auto border-collapse border border-gray-300 w-full text-left">
                <thead className="bg-gray-200">
                    <tr>
                        <td className="border border-gray-300 px-4 py-2">Số thứ tự</td>
                        <td className="border border-gray-300 px-4 py-2">Tên thuốc</td>
                        {!formResponse && <td className="border border-gray-300 px-4 py-2">Số lượng trong kho</td>}
                        <td className="border border-gray-300 px-4 py-2">Cách dùng</td>
                        <td className="border border-gray-300 px-4 py-2">Số lượng tư vấn</td>
                    </tr>
                </thead>
                <tbody>
                {[...selectedMedicinesMap].map(([key, value], index) => {
                    const detailAtIndex = `details[${index}].`

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
                            <td>{value.medicine ? value.medicine.name : value.name}</td>
                            {!formResponse && <td>{value.quantity}</td>}
                            <td>
                                {!formResponse && <input
                                    className="w-3/4 border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                    value={value.treatment}
                                    onChange={(e) => onChangeUsageMedicine(key, e.target.value)}/>}

                                {formResponse && <p>{value.treatment}</p>}
                            </td>
                            <td>
                                {!formResponse &&
                                    <input type={"number"} onChange={(e) => onChangeQuantity(key, e.target.value)}/>}

                                {formResponse && <p>{value.quantity}</p>}
                            </td>

                            {!formResponse && <td>
                                <button
                                    className="bg-red-500 text-white font-semibold px-4 py-2 rounded hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-blue-300"
                                    onClick={() => onRemoveMedicine(key)}>Xóa
                                </button>
                            </td>}
                        </tr>
                    </>
                })}
                </tbody>
            </table>

            <PrintableComponent ref={printRef}/>
            {!formResponse && <button
                className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                onClick={() => {
                onClickCreateConsultationForm()
            }}>Tạo phiếu tư vấn thuốc</button>}
            {formResponse && <button
                className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 mt-10"
                onClick={() => handleDownloadPdf()}>In phiếu tư vấn</button>}
        </div>
    )
}

export {MedicineConsultation}