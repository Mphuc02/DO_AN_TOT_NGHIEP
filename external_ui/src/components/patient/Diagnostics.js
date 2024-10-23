import {useState, useEffect, useRef} from "react";
import styles from '../../layouts/body/style.module.css'
import {AI, EMPLOYYEE, HOSPITAL_INFORMATION} from "../../Constant";
import {SendApiService} from "../../service/SendApiService";

const CreateAppointmentModal = ({ isOpen, onClose, diseases, doctors }) => {
    const tomorrow = new Date()
    tomorrow.setDate(tomorrow.getDate() + 1)
    const formattedTomorrow = tomorrow.toISOString().split('T')[0];

    const [appointment, setAppointment] = useState({
        doctorId: '',
        appointmentDate: formattedTomorrow,
        description: '',
        images: '',
        diseasesIds: []
    })
    useEffect(() => {
        setAppointment({
            ...appointment,
            diseasesIds: diseases
        })
    }, [diseases]);

    if(!isOpen){
        return null
    }

    console.log(doctors)

    const handleCreateAppointment = () => {
        console.log(appointment)
    }
    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>&times;</button>
                <h1>Tạo thông tin lịch hẹn</h1>

                <table>
                    <tbody>
                        <tr>
                            <td>Chọn bác sĩ:</td>
                            <td>
                                <select
                                    onChange={(e) => setAppointment({...appointment, doctorId: e.target.value})}>
                                    <option value={""}>-----------</option>
                                    {doctors.map((doctor, index) => (
                                        <option key={index} value={doctor.id}>
                                            {doctor.fullName.firstName + " " + doctor.fullName.middleName + " " + doctor.fullName.lastName}
                                        </option>
                                    ))}
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td>Ngày đặt lịch(Định dạng: tháng-ngày-năm):</td>
                            <td><input value={appointment.appointmentDate}
                                       onChange={(e) => setAppointment({...appointment, appointmentDate: e.target.value})}
                                       type={"date"} min={formattedTomorrow}/></td>
                        </tr>

                        <tr>
                            <td>Triệu chứng</td>
                            <td><textarea /></td>
                        </tr>

                        <tr>
                            <td>Danh sách các bệnh được chuẩn đoán</td>
                            <td>
                                {diseases.map()}
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

    const [doctors, setDoctors] = useState([])
    const [isCreateModalOpen, setIsCreateModelOpen] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);
    const [diseases, setDiseases] = useState(new Map())
    const [detectedDiseases, setDetectedDiseases] = useState(new Set())

    const successSendImage = (response) => {
        const data = response.data
        console.log(data)
        imageDetails[0].processedImage = 'data:image/jpeg;base64,' + data.decodedImg
        imageDetails[0].detectedDiseases = data.diseases
        setImageDetails([...imageDetails])

        data.diseases.forEach(detected => {
            detectedDiseases.add(detected)
        })
        setDetectedDiseases(detectedDiseases)
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
            setSelectedImage(file);
            imageDetails[0] = imageDetail
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
            setDoctors(response.data)
        }, (error) => {

        })
    }

    const handleDeleteImageClick = (index) => {
        const newDetectedDiseases = new Set()
        const temp = imageDetails.filter((image, imageIndex) => {
            if(index === imageIndex){
                return false;
            }

            image.detectedDiseases.forEach(disease => {
                newDetectedDiseases.add(disease)
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
        setDetectedDiseases(newDetectedDiseases)
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
        <div>
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

            {detectedDiseases.size > 0 && (
                <div>
                    <div>Dưới đây là danh sách các bệnh phát hiện được</div>
                    <ul>
                        {[...detectedDiseases].map(key => (
                            <li key={key}>{diseases.get(key).name}</li>
                        ))}
                    </ul>
                </div>)}

            <button onClick={() => openCreateModal()}>Tạo lịch hẹn khám bệnh</button>
            <CreateAppointmentModal isOpen={isCreateModalOpen} onClose={closeCreateModal} diseases={detectedDiseases} doctors={doctors}/>
        </div>
    )
}

export {Diagnostics}