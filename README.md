Ứng Dụng Quản Lý Nhà Trọ
Giới thiệu
Ứng dụng Quản Lý Nhà Trọ là một dự án bài tập lớn được phát triển trên nền tảng Android, nhằm hỗ trợ chủ nhà trọ quản lý các hoạt động liên quan đến phòng trọ, khách thuê và tài chính một cách hiệu quả. Ứng dụng cung cấp giao diện thân thiện, tính năng tự động hóa và đồng bộ dữ liệu, giúp tiết kiệm thời gian và giảm thiểu sai sót trong quản lý.
Dự án được xây dựng bằng Kotlin (hoặc Java, tùy theo ngôn ngữ bạn dùng) và sử dụng SQLite hoặc Firebase để lưu trữ dữ liệu.
Tính năng chính
Ứng dụng bao gồm các tính năng sau:

Quản lý phòng trọ:

Thêm, xóa, sửa thông tin phòng (giá thuê, trạng thái: trống/đã thuê, tiện ích như wifi, điều hòa).
Hiển thị danh sách phòng bằng giao diện trực quan.


Quản lý khách thuê:

Lưu trữ thông tin khách thuê (họ tên, số điện thoại, CMND/CCCD, ngày vào ở).
Liên kết khách thuê với phòng trọ tương ứng.


Quản lý hóa đơn:

Tự động tính tiền thuê, điện, nước, internet dựa trên thông tin phòng và chỉ số sử dụng.
Tạo và lưu trữ hóa đơn hàng tháng.
Xem lịch sử hóa đơn theo phòng hoặc khách thuê.


Thông báo:

Gửi thông báo nhắc nhở thanh toán hóa đơn qua ứng dụng, email hoặc Zalo.
Thông báo phòng trống cho chủ trọ khi có phòng chưa được thuê.


Báo cáo thu chi:

Thống kê doanh thu, chi phí và lợi nhuận theo tháng hoặc năm.
Xuất báo cáo dưới dạng bảng hoặc biểu đồ (nếu có).


Quản lý hợp đồng:

Lưu trữ thông tin hợp đồng thuê (ngày bắt đầu, ngày kết thúc, điều khoản).
Theo dõi và cảnh báo khi hợp đồng sắp hết hạn.


Đăng ký tạm trú online:

Hỗ trợ nhập và gửi thông tin khách thuê lên dịch vụ công để đăng ký tạm trú.
Lưu trữ thông tin đã gửi để tra cứu.



Công nghệ sử dụng

Ngôn ngữ lập trình: Kotlin (hoặc Java).
Cơ sở dữ liệu: SQLite (hoặc Firebase cho đồng bộ dữ liệu đám mây).
Giao diện: Material Design, RecyclerView, Fragment.
Thư viện:
Room Database (cho SQLite).
Firebase Cloud Messaging (cho thông báo đẩy).
Retrofit (nếu có gọi API, ví dụ: dịch vụ công hoặc Zalo).


Công cụ phát triển: Android Studio, Git.

Cài đặt và chạy ứng dụng
Yêu cầu

Android Studio (phiên bản mới nhất được khuyến nghị).
Thiết bị Android chạy API 21 (Android 5.0) trở lên hoặc emulator.
Kết nối internet (nếu sử dụng Firebase hoặc API bên thứ ba).

Hướng dẫn cài đặt

Clone repository:
git clone https://github.com/<your-username>/<your-repo-name>.git


Mở dự án:

Mở Android Studio.
Chọn File > Open và chọn thư mục dự án vừa clone.


Cài đặt dependencies:

Đảm bảo file build.gradle đã được cấu hình đúng.
Nhấn Sync Project with Gradle Files trong Android Studio.


Cấu hình cơ sở dữ liệu:

Nếu dùng SQLite: Không cần cấu hình thêm.
Nếu dùng Firebase:
Tạo dự án trên Firebase Console.
Tải file google-services.json và đặt vào thư mục app/.
Cập nhật thông tin trong build.gradle.




Chạy ứng dụng:

Kết nối thiết bị Android hoặc khởi động emulator.
Nhấn Run trong Android Studio để cài đặt và chạy ứng dụng.



Cách sử dụng

Đăng nhập: Sử dụng tài khoản chủ trọ (nếu có tính năng đăng nhập).
Quản lý phòng:
Vào mục "Phòng trọ" để thêm/sửa/xóa phòng.
Cập nhật trạng thái phòng (trống/đã thuê).


Quản lý khách thuê:
Thêm thông tin khách thuê và liên kết với phòng.


Tạo hóa đơn:
Nhập chỉ số điện, nước hàng tháng.
Hệ thống tự động tạo hóa đơn và gửi thông báo.


Xem báo cáo:
Chọn mục "Báo cáo" để xem thống kê thu chi.


Hợp đồng và tạm trú:
Tạo hợp đồng trong mục "Hợp đồng".
Gửi thông tin đăng ký tạm trú qua mục "Dịch vụ công".



Cấu trúc dự án
├── app
│   ├── src
│   │   ├── main
│   │   │   ├── java/com/example/nhatro
│   │   │   │   ├── activity      # Các Activity (MainActivity, RoomActivity,...)
│   │   │   │   ├── adapter       # Adapter cho RecyclerView
│   │   │   │   ├── model        # Các Entity (Room, Tenant, Invoice,...)
│   │   │   │   ├── database     # Room Database và DAO
│   │   │   │   ├── fragment     # Các Fragment (RoomFragment, InvoiceFragment,...)
│   │   │   ├── res
│   │   │   │   ├── layout       # File XML giao diện
│   │   │   │   ├── values       # Theme, string, color
│   ├── build.gradle             # Cấu hình dependencies
├── README.md                    # File này

Kế hoạch phát triển

Phiên bản hiện tại: Hỗ trợ quản lý cơ bản (phòng, khách, hóa đơn, thông báo).
Tương lai:
Thêm tính năng xuất hóa đơn thành PDF.
Tích hợp thanh toán online qua ví điện tử (Momo, ZaloPay).
Hỗ trợ đa ngôn ngữ (tiếng Anh, tiếng Việt).
Tối ưu hiệu suất và giao diện trên các thiết bị khác nhau.



Đóng góp
Chúng tôi hoan nghênh mọi ý kiến đóng góp! Để đóng góp:

Fork repository này.
Tạo branch mới: git checkout -b feature/<tên-tính-năng>.
Commit thay đổi: git commit -m "Mô tả thay đổi".
Push lên branch: git push origin feature/<tên-tính-năng>.
Tạo Pull Request trên GitHub.

Liên hệ
Nếu bạn có câu hỏi hoặc cần hỗ trợ, liên hệ qua:

Email: your-email@example.com
GitHub Issues: Tạo issue mới

Giấy phép
Dự án được phát hành dưới MIT License.

Cảm ơn bạn đã quan tâm đến dự án Quản Lý Nhà Trọ!
