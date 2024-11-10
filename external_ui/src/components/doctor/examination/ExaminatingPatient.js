import {useEffect, useState} from "react";
import {SendApiService} from "../../../service/SendApiService";
import {ExaminationResult, HOSPITAL_INFORMATION, PATIENT} from "../../../ApiConstant";
import RoutesConstant from "../../../RoutesConstant";
import {Link} from "react-router-dom";
import styles from '../../../layouts/body/style.module.css'

const HistoriesExamiantion = () => {

}

const SelectDiseaseModal = ({isOpen, onClose, diseasesMap, selectCallBack}) => {
    const [queryString, setQueryString] = useState('')
    const [queriedDiseases, setQueriedDiseases] = useState(null)

    useEffect(() => {
        setQueriedDiseases([...diseasesMap].map(([key, value]) => value))
    }, [diseasesMap])

    useEffect(() => {
        const query = queryString.toLowerCase()
        const temp = [...diseasesMap].filter(([key, value]) => {
            return value.name.toLowerCase().includes(query);
        }).map(([key, value]) => value);
        setQueriedDiseases(temp)
    }, [queryString])

    if(!isOpen){
        return null
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>&times;</button>

                <table>
                    <thead></thead>
                    <tbody>
                        <tr>
                            <td>Nhập tên bệnh: </td>
                            <td><input value={queryString} onChange={(e) => {setQueryString(e.target.value)}}/></td>
                        </tr>

                        <tr>
                            <td>Tên bệnh</td>
                            <td>Mô tả</td>
                        </tr>

                        {queriedDiseases.map(disease => {
                            return <tr key={disease.id}>
                                        <td>{disease.name}</td>
                                        <td>{disease.description}</td>
                                        <td><button onClick={() => selectCallBack(disease.id)}>Chọn bệnh này</button></td>
                                    </tr>
                        })}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

const Examination = ({examinationResult}) => {
    const [thisExaminationResult, setThisExaminationResult] = useState({...examinationResult})
    const [diseasesMap, setDiseasesMap] = useState(new Map())
    const [selectedDiseasesMap, setSelectedDiseasesMap] = useState(new Map())
    const [isSelectDiseaseOpen, setIsSelectDiseaseOpen] = useState(false)

    const getDiseases = () => {
        SendApiService.getRequest(HOSPITAL_INFORMATION.DISEASES.getUrl(), {}, response => {
            const tempMap = new Map()
            for(const disease of response.data){
                tempMap.set(disease.id, disease)
            }
            setDiseasesMap(tempMap)
        }, error => {

        })
    }

    useEffect(() => {
        setThisExaminationResult({...examinationResult})
    }, [examinationResult]);

    useEffect(() => {
        getDiseases()
    }, [])

    //Phần lựa chọn bệnh
    const onCloseSelectDieseaseModal = () => {
        setIsSelectDiseaseOpen(false)
    }

    const onOpenSelectDiseaseModal = () => {
        setIsSelectDiseaseOpen(true)
    }

    const setSelectedDisease = (id) => {
        const tempDiseasesMap = new Map(diseasesMap)
        const tempSelectedDiseasesMap = new Map(selectedDiseasesMap)

        tempSelectedDiseasesMap.set(id, tempDiseasesMap.get(id))
        tempDiseasesMap.delete(id)

        setDiseasesMap(tempDiseasesMap)
        setSelectedDiseasesMap(tempSelectedDiseasesMap)
        onCloseSelectDieseaseModal()
    }

    const deleteSelectedDisease = (id) => {
        const tempDiseasesMap = new Map(diseasesMap); // Tạo bản sao mới
        const tempSelectedDiseasesMap = new Map(selectedDiseasesMap); // Tạo bản sao mới

        tempDiseasesMap.set(id, tempSelectedDiseasesMap.get(id));
        tempSelectedDiseasesMap.delete(id);

        setDiseasesMap(tempDiseasesMap);
        setSelectedDiseasesMap(tempSelectedDiseasesMap);
    }

    const onChangeDiseaseDescriptionAtIndex = (index, value) => {
        const tempMap = new Map(selectedDiseasesMap)
        const diseaseAtIndex = tempMap.get(index)
        selectedDiseasesMap.set(index, {...diseaseAtIndex, diseaseDescription: value})
    }

    const onClickSaveResult = () => {

    }

    return (
        <div>
            <table>
                <thead>
                    <tr>
                        <td></td>
                        <td></td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Bệnh nhân:</td>
                        <td>{(() => {
                           if(!thisExaminationResult || !thisExaminationResult.patient){
                               return <p></p>
                           }
                           const fullName = thisExaminationResult.patient.fullName
                           return fullName.firstName + ' ' + fullName.middleName + ' ' + fullName.lastName
                        })()}</td>
                    </tr>

                    <tr>
                        <td>Triệu chứng: </td>
                        <td>{thisExaminationResult.symptom}</td>
                    </tr>
                </tbody>
            </table>

            <table>
                <thead>
                    <tr><td>Hình ảnh chuẩn đoán</td></tr>
                    <tr>
                        <td>Hình ảnh</td>
                        <td>Kết quả chuẩn đoán</td>
                    </tr>
                </thead>
                <tbody>
                    {(() => {
                        if (!thisExaminationResult || !thisExaminationResult.images) {
                            return <tr><td></td><td></td></tr>;
                        }

                        return thisExaminationResult.images.map(image => (
                            <tr key={image.id}>
                                <td><img className={styles.previewImage} src={image.image} alt="Original" /></td>
                                <td><img className={styles.previewImage} src={image.processedImage} alt="Processed" /></td>
                            </tr>
                        ));
                    })()}
                </tbody>
            </table>

            <table>
                <thead>
                    <tr>
                        <td></td>
                        <td></td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Điều trị:</td>
                        <td><textarea onChange={(e) => {setThisExaminationResult({...thisExaminationResult, treatment: e.target.value})}}/></td>
                    </tr>

                    <tr>
                        <td>Các bệnh phát hiện: </td>
                        <td><button onClick={() => onOpenSelectDiseaseModal()}>Lựa chọn bệnh</button></td>
                    </tr>

                    {[...selectedDiseasesMap].map(([key, value]) => {
                        if(!value){
                            return ''
                        }

                        return <tr key={key}>
                                    <td>{value.name}</td>
                                    <td><textarea onChange={(e) => onChangeDiseaseDescriptionAtIndex(key, e.target.value)}/></td>
                                    <td> <button onClick={() => deleteSelectedDisease(key)}>Xóa bệnh này</button> </td>
                        </tr>
                    })}
                </tbody>
            </table>

            <SelectDiseaseModal isOpen={isSelectDiseaseOpen} onClose={onCloseSelectDieseaseModal} diseasesMap={diseasesMap} selectCallBack={setSelectedDisease}/>

            <button onClick={() => onClickSaveResult()}>Lưu kết quả khám bệnh</button>
        </div>
    )
}

const ExaminatingPatient = () => {
    const examinationResultId = window.location.href.split('/').pop()
    const [examinationResult, setExaminationResult] = useState({})
    const [selectedTab, setSeletedTab] = useState(1)

    const getImageDetails = (id, examinationResult) => {
        return new Promise((resolve, reject) => {
            SendApiService.getRequest(
                PATIENT.APPOINTMENT.findAppointmentDetailByAppointmentId(id),
                {},
                response => {
                    examinationResult.images = response.data;
                    resolve(); // Đảm bảo Promise được giải quyết khi hoàn thành
                },
                error => {
                    reject(error); // Đảm bảo Promise bị từ chối khi có lỗi
                }
            );
        });
    };

    const getPatient = (patientId, examinationResult) => {
        return new Promise((resolve, reject) => {
            SendApiService.getRequest(
                PATIENT.PATIENT_API.findById(patientId),
                {},
                response => {
                    examinationResult.patient = response.data;
                    resolve(); // Đảm bảo Promise được giải quyết khi hoàn thành
                },
                error => {
                    reject(error); // Đảm bảo Promise bị từ chối khi có lỗi
                }
            );
        });
    };

    const getExamiantionResult = async () => {
        SendApiService.getRequest(
            ExaminationResult.ExaminationResultUrl.findById(examinationResultId),
            {},
            async response => {
                const tempResult = response.data;
                await getPatient(tempResult.patientId, tempResult);
                if (response.data.appointmentId) {
                    await getImageDetails(response.data.appointmentId, tempResult);
                }
                setExaminationResult(tempResult);
            },
            error => {
                // Xử lý lỗi tại đây nếu cần
            }
        );
    };


    useEffect(() => {
        getExamiantionResult()
    }, []);

    return (
        <div>
            <Link to={RoutesConstant.DOCTOR.EXAMINATION_MANAGEMENT}>Quay lại danh sách bệnh nhân chờ khám</Link>
            <h2>Khám bệnh cho bệnh nhân</h2>

            <div className={styles.divFlex}>
                <div className={styles.cursorPointer} onClick={() => setSeletedTab(1)}>Khám bệnh</div>
                <div className={styles.cursorPointer} onClick={() => setSeletedTab(2)}>Các lần khám trước</div>
                <div className={styles.cursorPointer} onClick={() => setSeletedTab(3)}>Phiếu tư vấn thuốc</div>
                <div className={styles.cursorPointer} onClick={() => setSeletedTab(4)}>Tạo phiếu hẹn khám lại</div>
            </div>

            {selectedTab === 1 && <Examination examinationResult={examinationResult}/>}
        </div>
    )
}

export {ExaminatingPatient}