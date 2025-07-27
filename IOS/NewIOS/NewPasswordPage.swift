import SwiftUI

struct NewPasswordPage: View {
    @State private var newPassword: String = ""
    @State private var confirmPassword: String = ""
    @Environment(\.dismiss) private var dismiss
    @State private var showNewPassword: Bool = false
    @State private var showConfirmPassword: Bool = false
    @State private var showPasswordMismatch: Bool = false
    @State private var showAlert = false
    @State private var alertMessage = ""
    @State private var showLoginPage = false
    var email: String = ""
    var code: String = ""
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
            
            // Başlık
            Text("New Password")
                .font(.system(size: 32, weight: .bold))
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.top, 8)
                .padding(.bottom, 16)
            
            // Şifre alanları
            VStack(alignment: .leading, spacing: 16) {
                Text("New Password").font(.headline).padding(.bottom, 0)
                ZStack(alignment: .trailing) {
                    if showNewPassword {
                        TextField("Your New Password", text: $newPassword)
                            .padding()
                            .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                    } else {
                        SecureField("Your New Password", text: $newPassword)
                            .padding()
                            .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                    }
                    if !newPassword.isEmpty {
                        Button(action: { showNewPassword.toggle() }) {
                            Image(systemName: showNewPassword ? "eye" : "eye.slash")
                                .foregroundColor(.gray)
                                .padding(.trailing, 12)
                        }
                    }
                }
                Spacer().frame(height: 0)
                Text("Confirm New Password").font(.headline).padding(.bottom, 0)
                ZStack(alignment: .trailing) {
                    if showConfirmPassword {
                        TextField("Confirm Your New Password", text: $confirmPassword)
                            .padding()
                            .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                    } else {
                        SecureField("Confirm Your New Password", text: $confirmPassword)
                            .padding()
                            .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                    }
                    if !confirmPassword.isEmpty {
                        Button(action: { showConfirmPassword.toggle() }) {
                            Image(systemName: showConfirmPassword ? "eye" : "eye.slash")
                                .foregroundColor(.gray)
                                .padding(.trailing, 12)
                        }
                    }
                }
                if showPasswordMismatch {
                    Text("Passwords do not match.")
                        .font(.caption)
                        .foregroundColor(.red)
                        .padding(.leading, 4)
                        .padding(.top, -6)
                }
            }
            .padding(.bottom, 24)
            
            // Save butonu
            Button(action: {
                if newPassword != confirmPassword {
                    showPasswordMismatch = true
                } else {
                    showPasswordMismatch = false
                    resetPassword()
                }
            }) {
                Text("Save")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color(red: 27/255, green: 38/255, blue: 69/255))
                    .foregroundColor(.white)
                    .cornerRadius(10)
                    .font(.title3)
            }
            .padding(.bottom, 8)
            // Cancel butonu
            Button(action: {
                dismiss()
            }) {
                Text("Cancel")
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
        .background(
            NavigationLink(destination: LoginPage().navigationBarBackButtonHidden(true), isActive: $showLoginPage) {
                EmptyView()
            }
            .hidden()
        )
        .alert(isPresented: $showAlert) {
            Alert(title: Text("Bilgi"), message: Text(alertMessage), dismissButton: .default(Text("Tamam"), action: {
                if showLoginPage { dismiss() }
            }))
        }
    }

    func resetPassword() {
        guard !email.isEmpty, !code.isEmpty, !nationalId.isEmpty, !newPassword.isEmpty, !confirmPassword.isEmpty else {
            alertMessage = "Tüm alanları doldurun."
            showAlert = true
            return
        }
        guard let url = URL(string: "http://34.32.107.243:8080/api/auth/reset-password") else { return }
        let body: [String: Any] = [
            "email": email,
            "code": code,
            "newPassword": newPassword,
            "nationalId": nationalId,
            "confirmPassword": confirmPassword
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
                    if message.lowercased().contains("success") || message.lowercased().contains("başarılı") {
                        alertMessage = "Your password has been successfully reset."
                        showAlert = true
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                            showLoginPage = true
                        }
                    } else {
                        alertMessage = message
                        showAlert = true
                    }
                } else if let str = String(data: data, encoding: .utf8), str.lowercased().contains("success") || str.lowercased().contains("başarılı") {
                    alertMessage = "Your password has been successfully reset."
                    showAlert = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                        showLoginPage = true
                    }
                } else {
                    alertMessage = "Şifre değiştirilemedi. Lütfen tekrar deneyin."
                    showAlert = true
                }
            }
        }.resume()
    }
}

struct NewPasswordPage_Previews: PreviewProvider {
    static var previews: some View {
        NewPasswordPage()
    }
} 
 
