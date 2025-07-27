import SwiftUI

struct ForgotPasswordPage: View {
    @State private var tcKimlik: String = ""
    @State private var email: String = ""
    @Environment(\.dismiss) private var dismiss
    @State private var showCodeVerificationPage = false
    @State private var showAlert = false
    @State private var alertMessage = ""
    @State private var tcTouched = false
    @State private var emailTouched = false
    @FocusState private var tcFieldFocused: Bool
    @FocusState private var emailFieldFocused: Bool
    @State private var showTopAlert = false
    
    var body: some View {
        VStack(spacing: 0) {
            // Logo ve başlık
            VStack(spacing: 4) {
                Image("Logo")
                    .resizable()
                    .scaledToFit()
                    .frame(height: 40)
                    .padding(.top, 24)
                    .frame(maxWidth: .infinity, alignment: .leading)
         
            }
            Text("Forgot Password")
                .font(.system(size: 32, weight: .bold))
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.top, 8)
                .padding(.bottom, 16)
            // Alanlar
            VStack(alignment: .leading, spacing: 16) {
                Text("National ID").font(.headline).padding(.bottom, 0)
                ZStack(alignment: .trailing) {
                    TextField("Your National ID", text: $tcKimlik, onEditingChanged: { editing in
                        if !editing { tcTouched = true }
                    })
                        .padding(.trailing, 36)
                    .padding()
                    .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                    .keyboardType(.numberPad)
                        .focused($tcFieldFocused)
                    Image(systemName: "checkmark.circle")
                        .foregroundColor(
                            tcKimlik.count == 11 && tcKimlik.allSatisfy({ $0.isNumber }) ? Color.green :
                            (tcTouched && !tcFieldFocused ? Color.red : Color.gray.opacity(0.4))
                        )
                        .padding(.trailing, 16)
                        .opacity(tcKimlik.isEmpty ? 0 : 1)
                }
                if tcTouched && !(tcKimlik.count == 11 && tcKimlik.allSatisfy({ $0.isNumber })) {
                    Text("National ID must be 11 digits and contain only numbers.")
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.leading, 4)
                        .padding(.top, -6)
                }
                Text("Mail").font(.headline).padding(.bottom, 0)
                ZStack(alignment: .trailing) {
                    TextField("Your Mail", text: $email, onEditingChanged: { editing in
                        if !editing { emailTouched = true }
                    })
                        .padding(.trailing, 36)
                    .padding()
                    .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                    .keyboardType(.emailAddress)
                        .focused($emailFieldFocused)
                    Image(systemName: "checkmark.circle")
                        .foregroundColor(
                            isValidEmail(email) ? Color.green :
                            (emailTouched && !emailFieldFocused ? Color.red : Color.gray.opacity(0.4))
                        )
                        .padding(.trailing, 16)
                        .opacity(email.isEmpty ? 0 : 1)
                }
                if emailTouched && !isValidEmail(email) {
                    Text("Please enter a valid e-mail address.")
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.leading, 4)
                        .padding(.top, -6)
                }
            }
            .padding(.bottom, 24)
            // Send Recovery Email butonu
            Button(action: {
                if tcKimlik.isEmpty && email.isEmpty {
                    showTopAlert = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                        showTopAlert = false
                    }
                } else {
                    // Hatalı veya eksik inputlar için touched'ı true yap
                    if !(tcKimlik.count == 11 && tcKimlik.allSatisfy({ $0.isNumber })) { tcTouched = true }
                    if !isValidEmail(email) { emailTouched = true }
                    if tcKimlik.count == 11 && tcKimlik.allSatisfy({ $0.isNumber }) && isValidEmail(email) {
                        sendForgotPasswordRequest()
                    }
                }
            }) {
                Text("Send Recovery Email")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color(red: 27/255, green: 38/255, blue: 69/255))
                    .foregroundColor(.white)
                    .cornerRadius(10)
                    .font(.title3)
            }
            .padding(.bottom, 8)
            // Sayfa geçişi için NavigationLink
            NavigationLink(destination: CodeVerificationPage(email: email, nationalId: tcKimlik), isActive: $showCodeVerificationPage) {
                EmptyView()
            }
            .hidden()
            // Back to Login butonu
            Button(action: {
                dismiss()
            }) {
                Text("Back to Login")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color(red: 80/255, green: 82/255, blue: 90/255))
                    .foregroundColor(.white)
                    .cornerRadius(10)
                    .font(.title3)
            }
            .padding(.bottom, 8)
            Spacer(minLength: 16)
        }
        .padding(.horizontal, 24)
        .navigationBarBackButtonHidden(true)
        .overlay(
            ZStack {
                if showTopAlert {
                    HStack {
                        Spacer(minLength: 0)
                        Text("Please fill in all fields.")
                            .font(.system(size: 18, weight: .semibold))
                            .foregroundColor(.white)
                            .multilineTextAlignment(.center)
                            .shadow(color: Color.black.opacity(0.55), radius: 4, x: 0, y: 2)
                        Spacer(minLength: 0)
                    }
                    .padding(.vertical, 16)
                    .padding(.horizontal, 24)
                    .background(
                        ZStack {
                            BlurView(style: .systemUltraThinMaterial)
                                .clipShape(RoundedRectangle(cornerRadius: 16))
                            RoundedRectangle(cornerRadius: 16)
                                .fill(Color(red: 32/255, green: 44/255, blue: 79/255).opacity(0.82))
                        }
                    )
                    .shadow(color: Color.black.opacity(0.10), radius: 6, x: 0, y: 2)
                    .frame(maxWidth: 340)
                    .padding(.top, 36)
                    .padding(.horizontal, 0)
                    .transition(.move(edge: .top).combined(with: .opacity))
                    .zIndex(10)
                }
            }, alignment: .top
        )
    }
    
    // Email doğrulama fonksiyonu
    func isValidEmail(_ email: String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPred = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailPred.evaluate(with: email)
    }

    // Şifre sıfırlama isteği fonksiyonu
    func sendForgotPasswordRequest() {
        guard let url = URL(string: "http://34.32.107.243:8080/api/auth/forgot-password") else { return }
        let body: [String: Any] = [
            "nationalId": tcKimlik,
            "email": email
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
                    if message == "Verification code sent to email." {
                        showCodeVerificationPage = true
                    } else {
                        alertMessage = message
                        showAlert = true
                    }
                } else if let str = String(data: data, encoding: .utf8), str.contains("Verification code sent to email.") {
                    showCodeVerificationPage = true
                } else {
                    alertMessage = "Beklenmeyen sunucu yanıtı."
                    showAlert = true
                }
            }
        }.resume()
    }
}

struct ForgotPasswordPage_Previews: PreviewProvider {
    static var previews: some View {
        ForgotPasswordPage()
    }
} 
