import SwiftUI

struct PackageInfo: Identifiable {
    let id = UUID()
    let name: String
    let type: String
    let used: Double
    let total: Double
    let expireDate: String
    let unit: String
}

struct BalanceResponse: Decodable {
    let balanceId: Int
    let msisdn: String
    let leftData: Double
    let leftSms: Int
    let leftMinutes: Int
    let getsDate: String
    let packageEntity: PackageEntity
}

struct PackageEntity: Decodable {
    let id: Int
    let dataQuota: Double
    let smsQuota: Int
    let minutesQuota: Int
    let price: Double
    let period: Int
    let packageName: String
}

struct CustomerResponse: Decodable {
    let msisdn: String
    let name: String?
    let surname: String?
    let email: String?
    let password: String?
    let sDate: String?
    let nationalId: String?
    let packageEntity: PackageEntity?
}

struct RemainingUsagePage: View {
    var msisdn: String? = nil
    @State private var userName: String = ""
    @State private var userSurname: String = ""
    @State private var balance: BalanceResponse? = nil
    @State private var isLoading: Bool = true
    @State private var isLoggedOut: Bool = false
    @State private var isCustomerLoading: Bool = true
    
    var body: some View {
        NavigationStack {
        ScrollView {
                VStack(alignment: .leading, spacing: 8) {
                    HStack(alignment: .top) {
                        Image("Logo")
                            .resizable()
                            .scaledToFit()
                            .frame(height: 48)
                            .padding(.top, 8)
                        Spacer()
                        Button(action: {
                            isLoggedOut = true
                        }) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(Color(red: 245/255, green: 245/255, blue: 247/255))
                                    .frame(width: 44, height: 44)
                                Image("Signout")
                        .resizable()
                                    .scaledToFit()
                                    .frame(width: 24, height: 24)
                            }
                            .padding(.top, 8)
                        }
                    }
                    .padding(.top, 8)

                    VStack(alignment: .leading, spacing: 0) {
                        Text("Welcome")
                            .font(.system(size: 36, weight: .bold))
                        if isCustomerLoading {
                            Text("...")
                                .font(.system(size: 36, weight: .medium))
                                .foregroundColor(.gray)
                        } else {
                            Text("\(userName) \(userSurname)")
                                .font(.system(size: 36, weight: .medium))
                                .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                        }
                    }
                    .padding(.top, 8)
                    // Remaining Usage başlığı
                Text("Remaining Usage")
                        .font(.system(size: 26, weight: .bold))
                        .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                    .padding(.vertical, 8)
                    // Kartlar
                if isLoading {
                    ProgressView()
                        .frame(maxWidth: .infinity)
                        .padding()
                } else if let balance = balance {
                    VStack(spacing: 16) {
                        // Data
                        HStack(alignment: .center, spacing: 0) {
                            let leftDataGB = balance.leftData / 1000.0
                            let dataQuotaGB = balance.packageEntity.dataQuota / 1000.0
                            let (endDate, daysLeft) = daysLeftFromStart(sdate: balance.getsDate, period: balance.packageEntity.period)
                            let endDateString: String = {
                                if let endDate = endDate {
                                    let df = DateFormatter()
                                    df.dateFormat = "dd/MM/yyyy"
                                    return df.string(from: endDate)
                } else {
                                    return "-"
                                }
                            }()
                            CircleProgressView(progress: dataQuotaGB == 0 ? 0 : leftDataGB / dataQuotaGB)
                                    .frame(width: 120, height: 120)
                                    .padding(.leading, 16)
                                VStack(alignment: .leading, spacing: 8) {
                                    HStack(alignment: .top) {
                                    Text(balance.packageEntity.packageName)
                                            .font(.system(size: 22, weight: .bold))
                                            .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                                        Spacer()
                                    Text("Data")
                                    .font(.subheadline)
                                    .foregroundColor(.gray)
                                            .padding(.top, 4)
                                    }
                                HStack {
                                    Text(String(format: "%.2fGB/", leftDataGB))
                                                .font(.system(size: 18, weight: .regular))
                                    + Text(String(format: "%.2fGB", dataQuotaGB))
                                                .font(.system(size: 18, weight: .bold))
                                }
                                .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                                VStack(alignment: .leading, spacing: 2) {
                                    Text("Expires on \(endDateString)")
                                        .font(.caption)
                                        .foregroundColor(.gray)
                                    Text("(\(daysLeft) days left)")
                                        .font(.caption2)
                                        .foregroundColor(.gray)
                                }
                            }
                            .padding(.leading, 20)
                            .padding(.trailing, 16)
                            .padding(.vertical, 24)
                        }
                        .background(Color.white)
                        .cornerRadius(24)
                        .shadow(color: Color.black.opacity(0.08), radius: 12, x: 0, y: 4)
                        .padding(.bottom, 0)
                        // Minutes
                        HStack(alignment: .center, spacing: 0) {
                            CircleProgressView(progress: Double(balance.leftMinutes) / Double(balance.packageEntity.minutesQuota == 0 ? 1 : balance.packageEntity.minutesQuota))
                                .frame(width: 120, height: 120)
                                .padding(.leading, 16)
                            VStack(alignment: .leading, spacing: 8) {
                                HStack(alignment: .top) {
                                    Text(balance.packageEntity.packageName)
                                        .font(.system(size: 22, weight: .bold))
                                        .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                                    Spacer()
                                    Text("Minute")
                                        .font(.subheadline)
                                        .foregroundColor(.gray)
                                        .padding(.top, 4)
                                }
                                HStack {
                                    Text("\(balance.leftMinutes)/")
                                                .font(.system(size: 18, weight: .regular))
                                    + Text("\(balance.packageEntity.minutesQuota)")
                                                .font(.system(size: 18, weight: .bold))
                                }
                                .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                                VStack(alignment: .leading, spacing: 2) {
                                    let (endDate, daysLeft) = daysLeftFromStart(sdate: balance.getsDate, period: balance.packageEntity.period)
                                    let endDateString: String = {
                                        if let endDate = endDate {
                                            let df = DateFormatter()
                                            df.dateFormat = "dd/MM/yyyy"
                                            return df.string(from: endDate)
                                        } else {
                                            return "-"
                                        }
                                    }()
                                    Text("Expires on \(endDateString)")
                                        .font(.caption)
                                        .foregroundColor(.gray)
                                    Text("(\(daysLeft) days left)")
                                        .font(.caption2)
                                        .foregroundColor(.gray)
                                }
                            }
                            .padding(.leading, 20)
                            .padding(.trailing, 16)
                            .padding(.vertical, 24)
                        }
                        .background(Color.white)
                        .cornerRadius(24)
                        .shadow(color: Color.black.opacity(0.08), radius: 12, x: 0, y: 4)
                        .padding(.bottom, 0)
                        // SMS
                        HStack(alignment: .center, spacing: 0) {
                            CircleProgressView(progress: Double(balance.leftSms) / Double(balance.packageEntity.smsQuota == 0 ? 1 : balance.packageEntity.smsQuota))
                                .frame(width: 120, height: 120)
                                .padding(.leading, 16)
                            VStack(alignment: .leading, spacing: 8) {
                                HStack(alignment: .top) {
                                    Text(balance.packageEntity.packageName)
                                        .font(.system(size: 22, weight: .bold))
                                        .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                                    Spacer()
                                    Text("SMS")
                                        .font(.subheadline)
                                        .foregroundColor(.gray)
                                        .padding(.top, 4)
                                }
                                HStack {
                                    Text("\(balance.leftSms)/")
                                        .font(.system(size: 18, weight: .regular))
                                    + Text("\(balance.packageEntity.smsQuota)")
                                                .font(.system(size: 18, weight: .bold))
                                    }
                                    .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                                    VStack(alignment: .leading, spacing: 2) {
                                    let (endDate, daysLeft) = daysLeftFromStart(sdate: balance.getsDate, period: balance.packageEntity.period)
                                    let endDateString: String = {
                                        if let endDate = endDate {
                                            let df = DateFormatter()
                                            df.dateFormat = "dd/MM/yyyy"
                                            return df.string(from: endDate)
                                        } else {
                                            return "-"
                                        }
                                    }()
                                    Text("Expires on \(endDateString)")
                                    .font(.caption)
                                    .foregroundColor(.gray)
                                    Text("(\(daysLeft) days left)")
                                    .font(.caption2)
                                    .foregroundColor(.gray)
                            }
                        }
                                .padding(.leading, 20)
                                .padding(.trailing, 16)
                                .padding(.vertical, 24)
                            }
                            .background(Color.white)
                            .cornerRadius(24)
                            .shadow(color: Color.black.opacity(0.08), radius: 12, x: 0, y: 4)
                        .padding(.bottom, 0)
                    }
                }
                // My Plan bölümü
                Text("My Plan")
                        .font(.system(size: 26, weight: .bold))
                    .bold()
                    .padding(.top)
                if !isLoading, let balance = balance {
                    MyPlanCard(balance: balance)
                }
            }
            .padding()
        }
        .onAppear {
            fetchCustomerData()
            fetchUserData()
        }
            NavigationLink(destination: LoginPage().navigationBarBackButtonHidden(true), isActive: $isLoggedOut) { EmptyView() }
            .navigationBarBackButtonHidden(true)
        }
    }
    
    func fetchUserData() {
        guard let msisdn = msisdn else { return }
        isLoading = true
        let urlString = "http://34.32.107.243:8080/api/balances/\(msisdn)"
        guard let url = URL(string: urlString) else { return }
        URLSession.shared.dataTask(with: url) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    print("API Hatası: \(error.localizedDescription)")
                    self.isLoading = false
                    return
                }
                guard let data = data else {
                    self.isLoading = false
                    return
                }
                do {
                    let decoded = try JSONDecoder().decode(BalanceResponse.self, from: data)
                    self.balance = decoded
                } catch {
                    print("Decode hatası: \(error)")
                }
            self.isLoading = false
        }
        }.resume()
    }
    
    func fetchCustomerData() {
        guard let msisdn = msisdn else { return }
        isCustomerLoading = true
        let urlString = "http://34.32.107.243:8080/api/customer/\(msisdn)"
        guard let url = URL(string: urlString) else { return }
        URLSession.shared.dataTask(with: url) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    print("Customer API Hatası: \(error.localizedDescription)")
                    self.isCustomerLoading = false
                    return
                }
                guard let data = data else {
                    self.isCustomerLoading = false
                    return
                }
                do {
                    let decoded = try JSONDecoder().decode(CustomerResponse.self, from: data)
                    self.userName = decoded.name ?? ""
                    self.userSurname = decoded.surname ?? ""
                } catch {
                    print("Customer decode hatası: \(error)")
                }
                self.isCustomerLoading = false
            }
        }.resume()
    }
    
    func formatDate(_ dateStr: String) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        if let date = formatter.date(from: dateStr) {
            formatter.dateFormat = "MMMM dd"
            return formatter.string(from: date)
        }
        return dateStr
    }
    
    func daysLeft(_ dateStr: String) -> Int {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        if let date = formatter.date(from: dateStr) {
            let diff = Calendar.current.dateComponents([.day], from: Date(), to: date)
            return diff.day ?? 0
        }
        return 0
    }
}

struct CircleProgressView: View {
    var progress: Double // 0.0 - 1.0
    var body: some View {
        ZStack {
            Circle()
                .stroke(Color(red: 27/255, green: 38/255, blue: 69/255).opacity(0.12), lineWidth: 16)
            Circle()
                .trim(from: 0, to: progress)
                .stroke(Color(red: 27/255, green: 38/255, blue: 69/255), style: StrokeStyle(lineWidth: 16, lineCap: .round))
                .rotationEffect(.degrees(-90))
            Text(String(format: "%.1f%%", progress * 100))
                .font(.system(size: 20, weight: .bold))
                .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                .padding(8)
        }
    }
}

// MyPlanCard View
struct MyPlanCard: View {
    let balance: BalanceResponse
    var body: some View {
        VStack(spacing: 0) {
            // Lacivert başlık kısmı
            VStack(alignment: .center, spacing: 4) {
                Text(balance.packageEntity.packageName)
                    .font(.title2)
                    .bold()
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity, alignment: .center)
                let (endDate, daysLeft) = daysLeftFromStart(sdate: balance.getsDate, period: balance.packageEntity.period)
                let endDateString: String = {
                    if let endDate = endDate {
                        let df = DateFormatter()
                        df.dateFormat = "MMMM dd"
                        return df.string(from: endDate)
                    } else {
                        return "-"
                    }
                }()
                Text("Expires on \(endDateString)")
                    .font(.subheadline)
                    .foregroundColor(.white.opacity(0.8))
                    .frame(maxWidth: .infinity, alignment: .center)
                Text("(\(daysLeft) days left)")
                    .font(.caption)
                    .foregroundColor(.white.opacity(0.8))
                    .frame(maxWidth: .infinity, alignment: .center)
            }
            .padding()
            .frame(maxWidth: .infinity)
            .background(Color(red: 27/255, green: 38/255, blue: 69/255))
            .cornerRadius(20, corners: [.topLeft, .topRight])

            // Kartın geri kalanı
            VStack(spacing: 8) {
            HStack(alignment: .center) {
                Text("\(daysLeftFromStart(sdate: balance.getsDate, period: balance.packageEntity.period).daysLeft)")
                    .font(.title)
                    .bold()
                Text("days left")
                    .font(.body)
                    .foregroundColor(.gray)
                Spacer()
                }
                CapsuleProgressBar(progress: Double(daysLeftFromStart(sdate: balance.getsDate, period: balance.packageEntity.period).daysLeft) / Double(balance.packageEntity.period == 0 ? 1 : balance.packageEntity.period))
                    .frame(height: 14)
                    .padding(.vertical, 8)
                HStack {
                    Spacer()
                let (endDate, _) = daysLeftFromStart(sdate: balance.getsDate, period: balance.packageEntity.period)
                let endDateString: String = {
                    if let endDate = endDate {
                        let df = DateFormatter()
                        df.dateFormat = "dd/MM/yyyy"
                        return df.string(from: endDate)
                    } else {
                        return "-"
                    }
                }()
                Text(endDateString)
                    .font(.caption)
                    .foregroundColor(.gray)
            }

            HStack(spacing: 0) {
                VStack(spacing: 2) {
                        Text("\(balance.packageEntity.smsQuota)")
                        .font(.system(size: 22, weight: .bold))
                    Text("SMS")
                        .font(.system(size: 16))
                        .foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity)
                Divider()
                    .frame(height: 32)
                    .padding(.vertical, 0)
                VStack(spacing: 2) {
                        Text("\(Int(balance.packageEntity.dataQuota))")
                        .font(.system(size: 28, weight: .bold))
                    Text("GB")
                        .font(.system(size: 16))
                        .foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity)
                Divider()
                    .frame(height: 32)
                    .padding(.vertical, 0)
                VStack(spacing: 2) {
                        Text("\(balance.packageEntity.minutesQuota)")
                        .font(.system(size: 22, weight: .bold))
                    Text("Minute")
                        .font(.system(size: 16))
                        .foregroundColor(.gray)
                }
                .frame(maxWidth: .infinity)
            }
            }
            .padding()
        }
        .background(
            RoundedRectangle(cornerRadius: 20)
                .fill(Color.white)
                .shadow(radius: 4)
        )
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 0)
        .padding(.vertical, 24)
    }
    func formatDate(_ dateStr: String) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX"
        if let date = formatter.date(from: dateStr) {
            formatter.dateFormat = "MMMM dd"
            return formatter.string(from: date)
        }
        return dateStr
    }
    func formatDate2(_ dateStr: String) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX"
        if let date = formatter.date(from: dateStr) {
            formatter.dateFormat = "dd/MM/yyyy"
            return formatter.string(from: date)
        }
        return dateStr
    }
    func daysLeft(_ dateStr: String) -> Int {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX"
        if let date = formatter.date(from: dateStr) {
            let diff = Calendar.current.dateComponents([.day], from: Date(), to: date)
            return diff.day ?? 0
        }
        return 0
    }
}


struct RoundedCorner: Shape {
    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(
            roundedRect: rect,
            byRoundingCorners: corners,
            cornerRadii: CGSize(width: radius, height: radius)
        )
        return Path(path.cgPath)
    }
}

extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape(RoundedCorner(radius: radius, corners: corners))
    }
}

struct RemainingUsagePage_Previews: PreviewProvider {
    static var previews: some View {
        RemainingUsagePage()
    }
} 

struct CapsuleProgressBar: View {
    var progress: Double // 0.0 - 1.0
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                Capsule()
                    .fill(Color(hex: "#B7CAFF"))
                Capsule()
                    .fill(Color(hex: "#202C4F"))
                    .frame(width: geometry.size.width * CGFloat(progress))
            }
        }
    }
}

extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (255, 0, 0, 0)
        }
        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue: Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
} 

// Kalan gün hesaplama fonksiyonu (global scope)
func daysLeftFromStart(sdate: String, period: Int) -> (endDate: Date?, daysLeft: Int) {
    let formatter = DateFormatter()
    formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX"
    formatter.locale = Locale(identifier: "en_US_POSIX")
    if let startDate = formatter.date(from: sdate) {
        if let endDate = Calendar.current.date(byAdding: .day, value: period, to: startDate) {
            let diff = Calendar.current.dateComponents([.day], from: Date(), to: endDate)
            return (endDate, diff.day ?? 0)
        }
    }
    return (nil, 0)
} 

