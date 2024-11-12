import {useEffect, useState} from "react";
import {SendApiService} from "../../../service/SendApiService";
import {MEDICINE} from "../../../ApiConstant";
import styles from "../../../layouts/body/style.module.css";

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

const MedicineConsultation = ({}) => {
    const [isOpenChooseMedicineModal, setIsOpenChooseMedicineModal] = useState(false)
    const [selectedMedicinesMap, setSelectedMedicinesMap] = useState(new Map())

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

    return (
        <div>
            <button onClick={() => openChooseMedicineModal()}>Chọn thuốc</button>
            <ChooseMedicineModal isOpen={isOpenChooseMedicineModal} onClose={onCloseChooseMedicineModal}
                                 onChooseMedicine={onChooseMedicine}/>

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
                {[...selectedMedicinesMap].map(([key, value], index) => {

                    return <tr key={value.id}>
                        <td>{index + 1}</td>
                        <td>{value.name}</td>
                        <td>{value.description}</td>
                        <td>{value.origin.name}</td>
                        <td>
                            <button onClick={() => onRemoveMedicine(key)}>Xóa</button>
                        </td>
                    </tr>
                })}
                </tbody>
            </table>

            <button>Tạo phiếu tư vấn thuốc</button>
        </div>
    )
}

export {MedicineConsultation}