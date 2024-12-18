import {useState, useEffect, useRef} from "react";
import styles from '../../layouts/body/style.module.css'
import {AI, EMPLOYYEE, HOSPITAL_INFORMATION, PATIENT, WORKING_SCHEDULE} from "../../ApiConstant";
import {SendApiService} from "../../service/SendApiService";
import {ConvertBlobUrlToBase64} from '../../service/BlobService'

const CreateAppointmentModal = ({ isOpen, onClose, diseases, doctorsMap, imageDetails }) => {
    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    const formattedTomorrow = tomorrow.toISOString().split('T')[0];

    const [doctorsInDay, setDoctorsInDay] = useState([])
    const [errorAppointment, setErrorAppointment] = useState({
        doctorId: '',
        appointmentDate: '',
        description: '',
        images: '',
        diseasesIds: ''
    })
    const [appointment, setAppointment] = useState({
        doctorId: '',
        appointmentDate: formattedTomorrow,
        description: '',
        images: [],
        diseasesIds: []
    })

    useEffect(() => {
        setAppointment({
            ...appointment,
            diseasesIds: diseases
        })
    }, [diseases]);

    useEffect(() => {
        SendApiService.getRequest(WORKING_SCHEDULE.getSchedulesByDate(appointment.appointmentDate), {}, (response) => {
            setDoctorsInDay(response.data)
            console.log(response.data)
        }, (error) => {
        })
    }, [appointment.appointmentDate])

    if(!isOpen){
        return null
    }

    const handleCreateAppointment = async () => {
        appointment.diseasesIds = [...diseases.keys()];

        console.log(imageDetails)
        if(imageDetails[0].processedImage !== null && imageDetails[0].processedImage !== ''){
            const images = [];
            for (const detail of imageDetails) {
                let image = '';
                try {
                    image = await ConvertBlobUrlToBase64(detail.previewImage);
                } catch (error) {
                    console.error("Lỗi:", error);
                }

                images.push({
                    image: image,
                    processedImage: detail.processedImage
                });
            }
            appointment.images = images;
        }

        SendApiService.postRequest(
            PATIENT.APPOINTMENT.getUrl(),
            JSON.stringify(appointment),
            { 'Content-type': 'application/json' },
            (response) => {
                alert("Thêm lịch hẹn thành công");
                setErrorAppointment({
                    doctorId: '',
                    appointmentDate: '',
                    description: '',
                    images: '',
                    diseasesIds: ''
                })

                setAppointment({
                    doctorId: '',
                    appointmentDate: formattedTomorrow,
                    description: '',
                    images: [],
                    diseasesIds: []
                })
            },
            (error) => {
                if(error.status === 400){
                    console.log(error.response.data.fields)
                    setErrorAppointment(error.response.data.fields)
                    if(error.response.data.message !== null){
                        alert("Lỗi: " + error.response.data.message)
                    }
                }
            }
        );
    };

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>&times;</button>
                <h1>Tạo thông tin lịch hẹn</h1>

                <table>
                    <tbody>
                        <tr>
                            <td></td>
                            <td>{errorAppointment.doctorId}</td>
                        </tr>

                        <tr>
                            <td>Chọn bác sĩ:</td>
                            <td>
                                <select
                                    onChange={(e) => setAppointment({...appointment, doctorId: e.target.value})}>
                                    <option value={""}>-----------</option>
                                    {doctorsInDay.map((doctor, index) => (
                                        <option key={index} value={doctor.employeeId}>
                                            {(() => {
                                                const fullName = doctorsMap.get(doctor.employeeId)?.fullName || {};
                                                return `${fullName.firstName || ''} ${fullName.middleName || ''} ${fullName.lastName || ''}`;
                                            })()}
                                        </option>
                                    ))}
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td></td>
                            <td>{errorAppointment.appointmentDate}</td>
                        </tr>
                        <tr>
                            <td>Ngày đặt lịch(Định dạng: tháng-ngày-năm):</td>
                            <td><input value={appointment.appointmentDate}
                                       onChange={(e) => setAppointment({...appointment, appointmentDate: e.target.value})}
                                       type={"date"} min={formattedTomorrow}/></td>
                        </tr>

                        <tr>
                            <td></td>
                            <td>{errorAppointment.description}</td>
                        </tr>
                        <tr>
                            <td>Triệu chứng</td>
                            <td><textarea value={appointment.description} onChange={(e) => setAppointment({...appointment, description: e.target.value})}/></td>
                        </tr>

                        <tr>
                            <td></td>
                            <td>{errorAppointment.diseasesIds}</td>
                        </tr>
                        <tr>
                            <td>Danh sách các bệnh được chuẩn đoán</td>
                            <td>
                                {[...diseases].map(([key, value]) => (
                                    <div key={key}>{value}</div>
                                ))}
                            </td>
                        </tr>
                    </tbody>
                </table>

                <button onClick={() => handleCreateAppointment()}>Tạo lịch hẹn</button>
            </div>
        </div>
    );
}

let page = ''
const Diagnostics = () => {
    page = 'send-image'

    const fileInputRef = useRef(null)
    const [imageDetails, setImageDetails] = useState([{
        processedImage: '',
        previewImage: '',
        detectedDiseases: []
    }])

    const [doctorsMap, setDoctorsMap] = useState(new Map)
    const [isCreateModalOpen, setIsCreateModelOpen] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);
    const [diseases, setDiseases] = useState(new Map())
    const [detectedDiseasesMap, setDetectedDiseasesMap] = useState(new Set())

    const successSendImage = (response) => {
        const data = response.data
        console.log(data)
        imageDetails[0].processedImage = 'data:image/jpeg;base64,' + data.decodedImg
        imageDetails[0].detectedDiseases = data.diseases
        setImageDetails([...imageDetails])

        const tempMap = new Map(detectedDiseasesMap)
        data.diseases.forEach(detected => {
            tempMap.set(diseases.get(detected).id, detected)
        })
        setDetectedDiseasesMap(tempMap)
        setSelectedImage(null)
        fileInputRef.current.value = ''
    }

    const errorSendImage = (error) => {

    }

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file && file.type.startsWith('image/')) {
            const imageDetail = {
                processedImage: '',
                previewImage: URL.createObjectURL(file),
                detectedDiseases: []
            }

            if(imageDetails[0].processedImage != null && imageDetails[0].processedImage != ''){
                imageDetails.unshift(imageDetail)
            }else{
                imageDetails[0] = imageDetail
            }
            setSelectedImage(file);
        } else {
            alert("Please select a valid image file");
            setSelectedImage(null);
        }
    };

    const handleAddImage = () => {
        console.log('images', imageDetails)

        if(imageDetails.length === 0 || imageDetails[0].processedImage === null || imageDetails[0].processedImage === ''){
            alert("Lỗi: Ảnh hiện tại chưa được chuẩn đoán, không thể thêm 1 ảnh mới")
            return;
        }

        const newImage = {
            processedImage: '',
            previewImage: '',
            detectedDiseases: []
        }
        imageDetails.unshift(newImage)
        setImageDetails([...imageDetails])
        setSelectedImage(null)
    }

    const handleDiagnosticButton = async () => {
        if(selectedImage === null){
            alert("Lỗi: Ảnh không được bỏ trống")
        }else{
            const formData = new FormData()
            formData.append("image", selectedImage)
            await SendApiService.postRequest(AI.diagnostic(), formData, {}, successSendImage, errorSendImage)
        }
    }

    const getAllDiseases = async () => {
        await SendApiService.getRequest(HOSPITAL_INFORMATION.DISEASES.getUrl(), {}, (response => {
            const tempMap = new Map()
            response.data.forEach(disease => {
                tempMap.set(disease.name, disease)
            })
            console.log(tempMap)
            setDiseases(tempMap)
        }), (error) => {

        })
    }

    const getAllDoctor = async () => {
        await SendApiService.getRequest(EMPLOYYEE.getUrl('DOCTOR'), {}, (response) => {
            const tempMap = new Map()
            for(let doctor of response.data){
                tempMap.set(doctor.id, doctor)
            }
            setDoctorsMap(tempMap)
        }, (error) => {

        })
    }

    const handleDeleteImageClick = (index) => {
        const newDetectedDiseases = new Map()
        const temp = imageDetails.filter((image, imageIndex) => {
            if(index === imageIndex){
                URL.revokeObjectURL(image.previewImage)
                return false;
            }

            image.detectedDiseases.forEach(disease => {
                newDetectedDiseases.set(diseases.get(disease).id, disease)
            })
            return true;
        })
        if(temp.length === 0){
            temp.push({
                processedImage: '',
                previewImage: '',
                detectedDiseases: []
            })
        }

        setImageDetails(temp)
        setDetectedDiseasesMap(newDetectedDiseases)
    }

    useEffect(() => {
        getAllDiseases()
        getAllDoctor()
    }, [page])

    const openCreateModal = () => {
        setIsCreateModelOpen(true);
    };

    const closeCreateModal = () => {
        setIsCreateModelOpen(false);
    };

    return (
        <div className={"mt-10"}>
            <h2>Chuẩn đoán bệnh thông qua hình ảnh</h2>

            {imageDetails.map((image, index) => (
                <div key={index}>
                    {index === 0 && (
                        <div className={styles.divFlex}>
                            <div>
                                <input
                                    type="file"
                                    ref={fileInputRef}
                                    accept="image/*"
                                    onChange={handleImageChange}
                                />

                                <button onClick={() => handleDiagnosticButton()}>Chuẩn đoán</button>
                            </div>

                            <button onClick={() => handleAddImage()}>Thêm ảnh</button>
                        </div>
                    )}

                    <div className={styles.divFlex}>
                        <div>
                            <p>Ảnh đã chọn</p>
                            {imageDetails[index].previewImage && (
                                <img className={styles.previewImage} src={imageDetails[index].previewImage}
                                     alt="Ảnh đã chọn"/>
                            )}
                        </div>

                        <div>
                            <p>Kết quả chuẩn đoán</p>
                            {imageDetails[index].processedImage && (
                                <img className={styles.previewImage} src={imageDetails[index].processedImage}
                                     alt="Ảnh đã chọn"/>
                            )}
                        </div>

                        <button onClick={() => handleDeleteImageClick(index)}>Xóa ảnh</button>
                    </div>
                </div>
            ))}

            {detectedDiseasesMap.size > 0 && (
                <div>
                    <div>Dưới đây là danh sách các bệnh phát hiện được</div>
                    <ul>

                        {[...detectedDiseasesMap].map(([key, value]) => (
                            <li key={key}>{value}</li>
                        ))}
                    </ul>
                </div>)}

            <button onClick={() => openCreateModal()}>Tạo lịch hẹn khám bệnh</button>
            <CreateAppointmentModal isOpen={isCreateModalOpen} onClose={closeCreateModal} diseases={detectedDiseasesMap} doctorsMap={doctorsMap} imageDetails={imageDetails}/>
        </div>
    )
}

export {Diagnostics}