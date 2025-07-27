import SwiftUI

struct CodeVerificationPage: View {
    @State private var code: [String] = Array(repeating: "", count: 5)
    @FocusState private var focusedIndex: Int?
    @State private var showNewPasswordPage = false
    @Environment(\.dismiss) private var dismiss
    @State private var showAlert = false
    @State private var alertMessage = ""
    var email: String = ""
    var nationalId: String = ""
    
    var body: some View {
        VStack(spacing: 0) {
            // Logo
            Image("Logo")
                .resizable()
                .scaledToFit()
                .frame(height: 40)
                .padding(.top, 24)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            // Açıklama
            Text("Please enter the 5-digit password we sent to your e-mail account.")
                .font(.system(size: 16))
                .multilineTextAlignment(.leading)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.vertical, 24)
            
            // Kod kutuları
            HStack(spacing: 16) {
                ForEach(0..<5, id: \ .self) { index in
                    ZStack {
                        RoundedRectangle(cornerRadius: 18)
                            .stroke(Color(red: 27/255, green: 38/255, blue: 69/255), lineWidth: 2)
                            .frame(width: 56, height: 64)
                        RoundedRectangle(cornerRadius: 18)
                            .fill(Color(red: 245/255, green: 245/255, blue: 245/255))
                            .frame(width: 56, height: 64)
                        TextField("", text: $code[index])
                            .frame(width: 56, height: 64)
                            .font(.system(size: 28, weight: .bold, design: .monospaced))
                            .multilineTextAlignment(.center)
                            .keyboardType(.numberPad)
                            .focused($focusedIndex, equals: index)
                            .onChange(of: code[index]) { newValue in
                                if newValue.count > 1 {
                                    code[index] = String(newValue.prefix(1))
                                }
                                if !newValue.isEmpty && index < 4 {
                                    focusedIndex = index + 1
                                }
                            }
                    }
                }
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 16)
            
            // Resend code
            HStack {
                Spacer()
                Button("Resend code") {
                    // Kod tekrar gönderme işlemi
                }
                .font(.system(size: 15, weight: .semibold))
                .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                .padding(.trailing, 4)
            }
            .padding(.bottom, 32)
            
            // Confirm butonu
            Button(action: {
                verifyCode()
            }) {
                Text("Confirm")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color(red: 27/255, green: 38/255, blue: 69/255))
                    .foregroundColor(.white)
                    .cornerRadius(10)
                    .font(.title3)
            }
            .padding(.bottom, 8)
            .frame(maxWidth: .infinity)
            
            Spacer()
        }
        .padding(.horizontal, 24)
        .navigationBarBackButtonHidden(true)
        .background(
            NavigationLink(destination: NewPasswordPage(email: email, code: code.joined(), nationalId: nationalId), isActive: $showNewPasswordPage) {
                EmptyView()
            }
            .hidden()
        )
        .alert(isPresented: $showAlert) {
            Alert(title: Text("Uyarı"), message: Text(alertMessage), dismissButton: .default(Text("Tamam")))
        }
    }

    func verifyCode() {
        let codeString = code.joined()
        guard !email.isEmpty, codeString.count == 5 else {
            alertMessage = "Lütfen e-posta ve 5 haneli kodu giriniz."
            showAlert = true
            return
        }
        guard let url = URL(string: "http://34.32.107.243:8080/api/auth/verify-code") else { return }
        let body: [String: Any] = [
            "email": email,
            "code": codeString
        ]
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)
        URLSession.shared.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    alertMessage = "Bir hata oluştu: \(error.localizedDescription)"
                    showAlert = true
                    return
                }
                guard let data = data else {
                    alertMessage = "Sunucu hatası."
                    showAlert = true
                    return
                }
                if let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
                   let message = json["message"] as? String {
                    if message == "Code verified." {
                        showNewPasswordPage = true
                    } else {
                        alertMessage = message
                        showAlert = true
                    }
                } else if let str = String(data: data, encoding: .utf8), str.contains("Code verified.") {
                    showNewPasswordPage = true
                } else {
                    alertMessage = "Kod doğrulanamadı. Lütfen tekrar deneyin."
                    showAlert = true
                }
            }
        }.resume()
    }
}

struct CodeVerificationPage_Previews: PreviewProvider {
    static var previews: some View {
        CodeVerificationPage()
    }
} 
