import SwiftUI

struct Tariff: Identifiable, Decodable {
    let id: Int
    let name: String
    let dataQuota: Int
    let smsQuota: Int
    let minutesQuota: Int
    let price: Double
    let period: Int
    let packageName: String
    
    var features: [String] {
        ["\(dataQuota) MB data", "\(minutesQuota) minute", "\(smsQuota) sms"]
    }
    var priceString: String {
        String(format: "%.2f TL / %d days", price, period)
    }
}

struct ChooseTariffPage: View {
    let userInfo: SignupInfo
    
    @State private var tariffs: [Tariff] = []
    @State private var selectedTariffId: Int?
    @State private var isLoading = true
    @Environment(\.dismiss) private var dismiss
    @State private var goToLogin = false

    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                // Header
                VStack(alignment: .leading, spacing: 8) {
                    Image("Logo")
                        .resizable()
                        .scaledToFit()
                        .frame(height: 48)
                        .padding(.top, 12)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text("My Tariff")
                        .font(.system(size: 32, weight: .bold))
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text("Choose the perfect plan for your needs")
                        .font(.body)
                        .foregroundColor(.gray)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
                .padding(.bottom, 8)

                if isLoading {
                    ProgressView()
                        .padding(.top, 50)
                } else if tariffs.isEmpty {
                    Text("No tariffs found. Please try again later.")
                        .foregroundColor(.gray)
                        .padding(.top, 50)
                } else {
                    // Packages
                    ForEach(tariffs) { tariff in
                        TariffCard(
                            packageName: tariff.packageName,
                            price: tariff.priceString,
                            features: tariff.features,
                            isSelected: selectedTariffId == tariff.id
                        )
                        .onTapGesture {
                            selectedTariffId = tariff.id
                        }
                    }
                }
                
                // Buttons
                VStack(spacing: 12) {
                    Button(action: {
                        if selectedTariffId != nil {
                            registerUser()
                        }
                    }) {
                        Text(selectedTariffId == nil ? "Choose Tariff" : "Sign Up")
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(selectedTariffId == nil ? Color.gray.opacity(0.4) : Color(red: 27/255, green: 38/255, blue: 69/255))
                            .foregroundColor(.white)
                            .cornerRadius(10)
                            .font(.title3)
                            .opacity(selectedTariffId == nil ? 0.7 : 1.0)
                            .animation(.easeInOut(duration: 0.2), value: selectedTariffId)
                    }
                    .disabled(selectedTariffId == nil)
                    
                    Button(action: {
                        dismiss()
                    }) {
                        Text("Back")
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color(red: 80/255, green: 82/255, blue: 90/255))
                            .foregroundColor(.white)
                            .cornerRadius(10)
                            .font(.title3)
                    }
                }
                .padding(.top, 12)
            }
            .padding(.horizontal, 24)
        }
        .navigationBarHidden(true)
        .background(
            NavigationLink(destination: LoginPage().navigationBarBackButtonHidden(true), isActive: $goToLogin) {
                EmptyView()
            }
            .hidden()
        )
        .onAppear {
            fetchTariffs()
        }
    }
    
    func fetchTariffs() {
        isLoading = true
        guard let url = URL(string: "http://34.32.107.243:8080/api/packages/all") else {
            isLoading = false
            return
        }
        URLSession.shared.dataTask(with: url) { data, response, error in
            DispatchQueue.main.async {
                if let data = data {
                    do {
                        let decoded = try JSONDecoder().decode([Tariff].self, from: data)
                        self.tariffs = decoded
                    } catch {
                        print("Decode error:", error)
                        self.tariffs = []
                    }
                } else {
                    print("Network error:", error ?? "Unknown error")
                    self.tariffs = []
                }
                self.isLoading = false
            }
        }.resume()
    }
    
    func registerUser() {
        guard let selectedTariffId = selectedTariffId,
              let selectedTariff = tariffs.first(where: { $0.id == selectedTariffId }) else {
            // Optionally, show an alert to the user to select a package
            print("Please select a tariff.")
            return
        }
        
        print("--- Registration Details ---")
        print("Name: \(userInfo.name) \(userInfo.surname)")
        print("Email: \(userInfo.email)")
        print("Phone: \(userInfo.phone)")
        print("TC Kimlik: \(userInfo.tcKimlik)")
        print("Selected Tariff: \(selectedTariff.name) (\(selectedTariff.priceString))")
        print("--------------------------")
        
        // Kullanıcının paketini tanımla
        assignPackageToUser(msisdn: userInfo.phone, packageId: selectedTariff.id)
    }

    func assignPackageToUser(msisdn: String, packageId: Int) {
        guard let url = URL(string: "http://34.32.107.243:8080/api/balances/balances") else { return }
        let msisdnFiltered = msisdn.filter { $0.isNumber }
        let body: [String: Any] = [
            "MSISDN": msisdnFiltered,
            "PACKAGE_ID": packageId
        ]
        guard let jsonData = try? JSONSerialization.data(withJSONObject: body) else { return }
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        URLSession.shared.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    print("Package assign failed: \(error.localizedDescription)")
                    return
                }
                if let data = data, let str = String(data: data, encoding: .utf8) {
                    print("Package assign response: \(str)")
                }
                guard let httpResponse = response as? HTTPURLResponse else {
                    print("Package assign failed: No response.")
                    return
                }
                if httpResponse.statusCode == 200 || httpResponse.statusCode == 201 {
                    print("Package assigned successfully!")
                    goToLogin = true
                } else {
                    print("Package assign failed: \(HTTPURLResponse.localizedString(forStatusCode: httpResponse.statusCode))")
                }
            }
        }.resume()
    }
}

struct TariffCard: View {
    let packageName: String
    let price: String
    let features: [String]
    let isSelected: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(packageName)
                .font(.title2)
                .fontWeight(.bold)
            Text(price)
                .font(.headline)
                .foregroundColor(.white.opacity(0.8))
            
            VStack(alignment: .leading, spacing: 8) {
                ForEach(features, id: \.self) { feature in
                    HStack {
                        Image(systemName: "checkmark.circle.fill")
                            .foregroundColor(.white)
                        Text(feature)
                    }
                }
            }
            .padding(.top, 5)
        }
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background {
            if isSelected {
                Color(red: 55/255, green: 92/255, blue: 198/255)
            } else {
                LinearGradient(
                    gradient: Gradient(colors: [Color(red: 32/255, green: 44/255, blue: 79/255), Color(red: 37/255, green: 59/255, blue: 122/255)]),
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
            }
        }
        .foregroundColor(.white)
        .cornerRadius(15)
        .shadow(color: Color.black.opacity(0.08), radius: 12, x: 0, y: 4)
    }
}

struct ChooseTariffPage_Previews: PreviewProvider {
    static var previews: some View {
        ChooseTariffPage(userInfo: SignupInfo(name: "John", surname: "Doe", tcKimlik: "1234567890", email: "john.doe@example.com", phone: "5551234567", password: "password123"))
    }
} 
