
const GetTodayString = () => {
    const timeZone = 'Asia/Ho_Chi_Minh';

    const today = new Date().toLocaleString('en-CA', {
        timeZone: timeZone,
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });

    const todayTime = today.split(',')[0]; // Kết quả đã là "YYYY-MM-DD"
    return today
}

export {GetTodayString}