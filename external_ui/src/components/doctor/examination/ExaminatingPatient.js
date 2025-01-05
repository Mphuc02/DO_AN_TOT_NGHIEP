import {useEffect, useState} from "react";
import {SendApiService} from "../../../service/SendApiService";
import {ExaminationResult, HOSPITAL_INFORMATION, PATIENT} from "../../../ApiConstant";
import RoutesConstant from "../../../RoutesConstant";
import {Link} from "react-router-dom";
import styles from '../../../layouts/body/style.module.css'
import {jsPDF} from "jspdf";
import {MedicineConsultation} from "./MedicineConsultation";
import {AppointmentForm} from "./AppointmentForm";
import {PatientExaminationHistories} from "./PatientExaminationHistories";

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

                <tr>
                    <td>Nhập tên bệnh:</td>
                    <td><input className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                               value={queryString} onChange={(e) => {
                        setQueryString(e.target.value)
                    }}/></td>
                </tr>

                <table className="table-auto  w-full text-left">
                    <thead></thead>
                    <tbody>
                    <tr className="bg-gray-200">
                        <td className="border border-gray-300 px-4 py-2">Tên bệnh</td>
                        <td className="border border-gray-300 px-4 py-2">Mô tả</td>
                    </tr>

                    {queriedDiseases.map(disease => {
                        return <tr key={disease.id}>
                            <td className="border border-gray-300 px-4 py-2 text-center">{disease.name}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">{disease.description}</td>
                            <td className="border border-gray-300 px-4 py-2 text-center">
                                <button
                                    className="bg-blue-500 text-white font-semibold px-4 py-2 rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-300"
                                    onClick={() => selectCallBack(disease.id)}>Chọn bệnh này
                                </button>
                            </td>
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
    const [treatment, setTreatment] = useState('')
    const [examinationResultResponse, setExaminationResultResponse] = useState(null)
    const [error, setError] = useState({})
    let isClickSaveButton = false

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
        if(isClickSaveButton){
            return
        }

        isClickSaveButton = true

        const details = [...selectedDiseasesMap].map(([key, value]) => {
            return {
                diseaseId: key,
                diseaseDescription: value.diseaseDescription
            }
        })

        const examinationResult = {
            treatment: treatment,
            details: details
        }

        SendApiService.putRequest(ExaminationResult.ExaminationResultUrl.findById(thisExaminationResult.id), examinationResult,{ }, response => {
                setError({})
                setExaminationResultResponse(response.data)
                window.location.reload()
            }, error => {
                isClickSaveButton = false
                if(error.status === 400){
                    setError(error.response.data)
                }
            }
        )
    }

    const handlePrintExaminationResult = () => {
        console.log(examinationResult)
        const hospitalName = 'Benh vien da lieu Minh Phuc'
        const fullName = examinationResult.patient.fullName
        let dateOfBirth = examinationResult.patient.dateOfBirth
        if(dateOfBirth){
            dateOfBirth = examinationResult.patient.dateOfBirth.split('-').reverse().join('-')
        }

        const doc = new jsPDF();

        doc.setFont('helvetica', 'normal');

        doc.setFontSize(20);
        doc.text(hospitalName, 105, 20, { align: 'center' });

        doc.setFontSize(16);
        doc.text('Ket qua kham benh', 105, 40, { align: 'center' });

        doc.setFontSize(14);
        doc.text(`Ten benh nhan: ${fullName.firstName + " " + fullName.middleName + " " + fullName.lastName}`, 20, 70);
        doc.text(`Nam sinh: ${dateOfBirth}`, 20, 80)
        doc.text('Que quan: ', 20,90)
        doc.text(`Trieu chung: ${examinationResult.symptom}`, 20, 100)
        doc.text(`Dieu tri: ${examinationResult.treatment}`, 20, 110)
        doc.text('Ket qua kham benh:', 20, 130)

        const header = ['Số thứ tự', 'Tên bệnh', 'Kết quả'];
        const data = [
                {'Số thứ tự': '1', 'Tên bệnh': 'Bệnh A', 'Kết quả': 'Kết quả 1 jkhjha ajhshhdhjag aassuy tdajsyhg d jhagsd jhasg hj gajh gd ajhsgd jha d'},
                {'Số thứ tự': '2', 'Tên bệnh': 'Bệnh B', 'Kết quả': 'Kết quả 2'},
                {'Số thứ tự': '3', 'Tên bệnh': 'Bệnh C', 'Kết quả': 'Kết quả 3'}
        ];

        var config = {
            autoSize     : false,
            printHeaders : true,
            overflow: 'linebreak'
        }

        doc.table(10, 10, data, header, config);

        doc.autoPrint();
        window.open(doc.output('bloburl'), '_blank');
    }

    return (
        <div className="mt-5">
            <table>
                <thead>
                    <tr>
                        <td></td>
                        <td></td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Bệnh nhân:</td>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">{(() => {
                           if(!thisExaminationResult || !thisExaminationResult.patient){
                               return <p></p>
                           }
                           const fullName = thisExaminationResult.patient.fullName
                           return fullName.firstName + ' ' + fullName.middleName + ' ' + fullName.lastName
                        })()}</td>
                    </tr>

                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Triệu chứng: </td>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">{thisExaminationResult.symptom}</td>
                    </tr>
                </tbody>
            </table>

            {thisExaminationResult && thisExaminationResult.images && <table>
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
            </table> }

            <table>
                <thead>
                    <tr>
                        <td></td>
                        <td></td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td></td>
                        <td className="text-red-500 text-sm font-medium">{error.treatment}</td>
                    </tr>

                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Điều trị:</td>
                        {!examinationResult.examinatedAt && <td colSpan={2}><input className="w-1000 border-2 border-gray-800 rounded-md p-2 mb-2 mt-2" onChange={(e) => setTreatment(e.target.value)}/></td>}
                        {examinationResult.examinatedAt && <td colSpan={2}>{examinationResult.treatment}</td>}
                    </tr>

                    <tr>
                        <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Các bệnh mắc phải: </td>

                        {!examinationResult.examinatedAt && <td><button className="bg-blue-500 text-white font-semibold px-4 py-2 rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-300"
                            onClick={() => onOpenSelectDiseaseModal()}>Lựa chọn bệnh</button></td>}
                    </tr>

                    <tr>
                        <td></td>
                        <td className="text-red-500 text-sm font-medium">{error.details}</td>
                    </tr>

                </tbody>
            </table>

            <table border={1}>
                <tbody>
                {[...selectedDiseasesMap].map(([key, value], index) => {
                    if(!value){
                        return ''
                    }

                    const errorAtIndex = `details[${index}].diseaseDescription`
                    return <>
                        <tr>
                            <td></td>
                            <td className="text-red-500 text-sm font-medium">{error[errorAtIndex]}</td>
                        </tr>

                        <tr >
                            <td className="pl-2 pr-1 font-medium text-gray-700 text-left mb-4">{value.name}</td>
                            <td><input
                                className="w-full border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                onChange={(e) => onChangeDiseaseDescriptionAtIndex(key, e.target.value)}/>
                            </td>
                            <td>
                                <button className="bg-red-500 text-white font-semibold px-4 py-2 rounded hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-300"
                                        onClick={() => deleteSelectedDisease(key)}>Xóa bệnh này</button>
                            </td>
                        </tr>
                    </>
                })}
                {examinationResult.details && examinationResult.details.map(disease => {
                    if(!disease || !diseasesMap.get(disease.diseaseId)){
                        return null
                    }
                    return <tr key={disease.id}>
                            <td className=" pl-2 pr-1 font-medium text-red-500">{diseasesMap.get(disease.diseaseId).name}:</td>
                            <td>{disease.diseaseDescription}</td>
                    </tr>
                })}
                </tbody>
            </table>

            <SelectDiseaseModal isOpen={isSelectDiseaseOpen} onClose={onCloseSelectDieseaseModal}
                                diseasesMap={diseasesMap} selectCallBack={setSelectedDisease}/>

            {!examinationResult.examinatedAt &&
                <button className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                        onClick={() => onClickSaveResult()}>Lưu kết quả khám bệnh</button>}

            {examinationResult.examinatedAt &&
                <button className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 mt-10"
                        onClick={() => handlePrintExaminationResult()}>In kết quả khám bệnh</button>}
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
                    resolve();
                },
                error => {
                    reject(error);
                }
            );
        });
    };

    const getPatient = (patientId, examinationResult) => {
        return new Promise((resolve, reject) => {
            SendApiService.getRequest(
                PATIENT.PATIENT_API.byId(patientId),
                {},
                response => {
                    examinationResult.patient = response.data;
                    resolve();
                },
                error => {
                    reject(error);
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

            }
        );
    };


    useEffect(() => {
        getExamiantionResult()
    }, []);

    return (
        <div className="mt-10">
            <h2 className="text-xl font-bold text-green-600 mb-4">Khám bệnh cho bệnh nhân</h2>
            <Link className="bg-blue-500 text-white font-semibold rounded hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-300"
                to={RoutesConstant.DOCTOR.EXAMINATION_MANAGEMENT}>Quay lại danh sách bệnh nhân chờ khám</Link>
            <div className={styles.divFlex}>
                <div className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 1 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSeletedTab(1)}>Khám bệnh</div>
                <div className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 2 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSeletedTab(2)}>Các lần khám trước</div>
                <div className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 3 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSeletedTab(3)}>Phiếu tư vấn thuốc</div>
                <div className={`cursor-pointer py-2 px-4 rounded-lg transition-colors ${selectedTab === 4 ? 'bg-green-500 text-white' : 'hover:bg-gray-200'}`}
                    onClick={() => setSeletedTab(4)}>Tạo phiếu hẹn khám lại</div>
            </div>

            {selectedTab === 1 && <Examination examinationResult={examinationResult}/>}
            {selectedTab === 2 && <PatientExaminationHistories examinationResult={examinationResult} />}
            {selectedTab === 3 && <MedicineConsultation />}
            {selectedTab === 4 && <AppointmentForm examinationResult={examinationResult} />}
        </div>
    )
}

export {ExaminatingPatient}