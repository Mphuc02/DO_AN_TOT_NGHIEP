import {useState, useEffect} from "react";
import styles from '../../layouts/body/style.module.css'
import WebSocketService from "../../service/WebSocketService";
import {AI, HOSPITAL_INFORMATION, WEBSOCKET} from "../../Constant";
import {JwtService} from "../../service/JwtService";
import {SendApiService} from "../../service/SendApiService";

let page = ''
const Diagnostics = () => {
    page = 'send-image'
    const [selectedImage, setSelectedImage] = useState(null);
    const [previewImage, setPreviewImage] = useState('');
    const [processedImage, setProcessedImage] = useState('')
    const [diseases, setDiseases] = useState([])
    const [detectedDiases, setDetectedDiseases] = useState([])

    const successSendImage = (response) => {
        const data = response.data
        setProcessedImage('data:image/jpeg;base64,' + data.decodedImg)
        setDetectedDiseases(data.diseases)
    }

    const errorSendImage = (error) => {

    }

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file && file.type.startsWith('image/')) {
            setSelectedImage(file);
            setProcessedImage(prev => {
                if(prev){
                    URL.revokeObjectURL(prev)
                }
                return ''
            })

            setPreviewImage((prev) => {
                if(prev){
                    URL.revokeObjectURL(prev)
                }
                return URL.createObjectURL(file)
            });
        } else {
            alert("Please select a valid image file");
            setSelectedImage(null);
            setPreviewImage('');
        }
    };

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
            setDiseases(response.data)
        }), (error) => {

        })
    }

    useEffect(() => {
        const webSocket = new WebSocketService()
        const topics = [WEBSOCKET.processedImage(JwtService.geUserFromToken())]

        webSocket.connectAndSubscribe(topics, (message, index) => {
            console.log(message)
        })

        getAllDiseases()
    }, [page])

    return (
        <div>
            <h2>Chuẩn đoán bệnh thông qua hình ảnh</h2>

            <input
                type="file"
                accept="image/*"
                onChange={handleImageChange}
            />

            <button onClick={() => handleDiagnosticButton()}>Chuẩn đoán</button>

            <div className={styles.divFlex}>
                <div>
                    <p>Ảnh đã chọn</p>
                    {selectedImage && (
                        <img className={styles.previewImage} src={previewImage} alt="Ảnh đã chọn"/>
                    )}
                </div>

                <div>
                    <p>Kết quả chuẩn đoán</p>
                    {processedImage && (
                        <img className={styles.previewImage} src={processedImage} alt="Ảnh đã chọn"/>
                    )}
                </div>
            </div>
        </div>
    )
}

export {Diagnostics}