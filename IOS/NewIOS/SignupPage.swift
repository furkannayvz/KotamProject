import SwiftUI

struct SignupInfo {
    var name: String
    var surname: String
    var tcKimlik: String
    var email: String
    var phone: String
    var password: String
}

struct SignupPage: View {
    @State private var name: String = ""
    @State private var surname: String = ""
    @State private var tcKimlik: String = ""
    @State private var email: String = ""
    @State private var password: String = ""
    @State private var confirmPassword: String = ""
    @State private var phone: String = ""
    
    @State private var nameTouched = false
    @State private var surnameTouched = false
    @State private var tcKimlikTouched = false
    @State private var emailTouched = false
    @State private var passwordTouched = false
    @State private var confirmPasswordTouched = false
    @State private var phoneTouched = false
    
    @FocusState private var passwordFieldFocused: Bool
    @FocusState private var passwordInputFocused: Bool
    @FocusState private var confirmPasswordInputFocused: Bool
    @FocusState private var tcKimlikFieldFocused: Bool
    
    @State private var showChooseTariff = false
    @State private var showAlert = false
    @State private var alertMessage = ""
    @State private var showTopAlert = false
    @State private var topAlertMessage = ""
    
    @Environment(\.dismiss) private var dismiss
 
    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                VStack(spacing: 4) {
                    Image("Logo")
                        .resizable()
                        .scaledToFit()
                        .frame(height: 40)
                        .padding(.top, 24)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
                Text("Sign Up")
                    .font(.system(size: 32, weight: .bold))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 8)
                    .padding(.bottom, 16)
                // Alanlar
                VStack(alignment: .leading, spacing: 16) {
                    Group {
                        Text("Name").font(.headline)
                            .padding(.bottom, -10)
                        ZStack(alignment: .trailing) {
                            TextField("Your Name", text: $name, onEditingChanged: { editing in
                                if !editing { nameTouched = true }
                            })
                                .padding(.trailing, 36)
                                .padding()
                                .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                            Image(systemName: "checkmark.circle")
                                .foregroundColor(name.count >= 2 ? Color.green : (nameTouched && !name.isEmpty ? Color.red : Color.gray.opacity(0.4)))
                                .padding(.trailing, 16)
                                .opacity(name.isEmpty ? 0 : 1)
                        }
                        Text("Surname").font(.headline)
                            .padding(.bottom, -10)
                        ZStack(alignment: .trailing) {
                            TextField("Your Surname", text: $surname, onEditingChanged: { editing in
                                if !editing { surnameTouched = true }
                            })
                                .padding(.trailing, 36)
                                .padding()
                                .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                            Image(systemName: "checkmark.circle")
                                .foregroundColor(surname.count >= 2 ? Color.green : (surnameTouched && !surname.isEmpty ? Color.red : Color.gray.opacity(0.4)))
                                .padding(.trailing, 16)
                                .opacity(surname.isEmpty ? 0 : 1)
                        }
                        Text("National ID").font(.headline)
                            .padding(.bottom, -10)
                        ZStack(alignment: .trailing) {
                            TextField("Your National ID", text: $tcKimlik, onEditingChanged: { editing in
                                if !editing { tcKimlikTouched = true }
                            })
                                .padding(.trailing, 36)
                                .padding()
                                .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                                .keyboardType(.numberPad)
                                .focused($tcKimlikFieldFocused)
                            Image(systemName: "checkmark.circle")
                                .foregroundColor(
                                    tcKimlik.count == 11 && tcKimlik.allSatisfy({ $0.isNumber }) ? Color.green :
                                    (tcKimlikTouched && !tcKimlikFieldFocused ? Color.red : Color.gray.opacity(0.4))
                                )
                                .padding(.trailing, 16)
                                .opacity(tcKimlik.isEmpty ? 0 : 1)
                        }
                        Text("Mail").font(.headline)
                            .padding(.bottom, -10)
                        ZStack(alignment: .trailing) {
                            TextField("Your Mail", text: $email, onEditingChanged: { editing in
                                if !editing { emailTouched = true }
                            })
                                .padding(.trailing, 36)
                                .padding()
                                .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                                .keyboardType(.emailAddress)
                            Image(systemName: "checkmark.circle")
                                .foregroundColor(isValidEmail(email) ? Color.green : (emailTouched && !email.isEmpty ? Color.red : Color.gray.opacity(0.4)))
                                .padding(.trailing, 16)
                                .opacity(email.isEmpty ? 0 : 1)
                        }
                        Text("Password").font(.headline)
                            .padding(.bottom, -10)
                        ZStack(alignment: .trailing) {
                            SecureField("Your Password", text: $password)
                                .padding(.trailing, 36)
                                .padding()
                                .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                                .focused($passwordInputFocused)
                                .onChange(of: passwordInputFocused) { focused in
                                    if !focused { passwordTouched = true }
                                }
                            Image(systemName: "checkmark.circle")
                                .foregroundColor(
                                    isPasswordValid ? Color.green :
                                    (passwordTouched && !passwordInputFocused ? Color.red : Color.gray.opacity(0.4))
                                )
                                .padding(.trailing, 16)
                                .opacity(password.isEmpty ? 0 : 1)
                        }
                        // Password requirements checklist
                        if passwordFieldFocused || !password.isEmpty {
                            let columns = [
                                GridItem(.flexible(), alignment: .leading),
                                GridItem(.flexible(), alignment: .leading)
                            ]
                            LazyVGrid(columns: columns, spacing: 12) {
                                PasswordRequirementRow(
                                    met: hasMinLength,
                                    text: "At least 8 characters",
                                    showError: !passwordFieldFocused && !hasMinLength
                                )
                                PasswordRequirementRow(
                                    met: hasUppercase,
                                    text: "At least one uppercase",
                                    showError: !passwordFieldFocused && !hasUppercase
                                )
                                PasswordRequirementRow(
                                    met: hasNumber,
                                    text: "At least one number",
                                    showError: !passwordFieldFocused && !hasNumber
                                )
                                PasswordRequirementRow(
                                    met: hasSymbol,
                                    text: "At least one symbol",
                                    showError: !passwordFieldFocused && !hasSymbol
                                )
                            }
                            .padding()
                            .background(Material.thin)
                            .cornerRadius(12)
                            .transition(.opacity.animation(.smooth))
                        }
                        Text("Confirm Password").font(.headline)
                            .padding(.bottom, -10)
                        ZStack(alignment: .trailing) {
                            SecureField("Confirm Your Password", text: $confirmPassword)
                                .padding(.trailing, 36)
                                .padding()
                                .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                                .focused($confirmPasswordInputFocused)
                                .onChange(of: confirmPasswordInputFocused) { focused in
                                    if !focused { confirmPasswordTouched = true }
                                }
                            Image(systemName: "checkmark.circle")
                                .foregroundColor(
                                    (!confirmPassword.isEmpty && confirmPassword == password) ? Color.green :
                                    (confirmPasswordTouched && !confirmPasswordInputFocused ? Color.red : Color.gray.opacity(0.4))
                                )
                                .padding(.trailing, 16)
                                .opacity(confirmPassword.isEmpty ? 0 : 1)
                        }
                    }
                    Text("Phone").font(.headline)
                        .padding(.bottom, -10)
                    ZStack(alignment: .trailing) {
                        TextField("Your Phone", text: $phone, onEditingChanged: { editing in
                            if !editing { phoneTouched = true }
                        })
                            .padding(.trailing, 36)
                            .padding()
                            .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                            .keyboardType(.phonePad)
                        Image(systemName: "checkmark.circle")
                            .foregroundColor(phone.filter { $0.isNumber }.count == 10 ? Color.green : (phoneTouched && !phone.isEmpty ? Color.red : Color.gray.opacity(0.4)))
                            .padding(.trailing, 16)
                            .opacity(phone.isEmpty ? 0 : 1)
                    }
                }
                .padding(.bottom, 16)
                // Sign Up butonu
                Button(action: {
                    validateAndProceed()
                }) {
                    Text("Continue")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color(red: 27/255, green: 38/255, blue: 69/255))
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .font(.title3)
                }
                .padding(.bottom, 8)
                NavigationLink(destination: ChooseTariffPage(userInfo: SignupInfo(name: name, surname: surname, tcKimlik: tcKimlik, email: email, phone: phone, password: password)), isActive: $showChooseTariff) {
                    EmptyView()
                }

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
        }
        .navigationBarBackButtonHidden(true)
        .overlay(
            ZStack {
                if showTopAlert {
                    HStack {
                        Spacer(minLength: 0)
                        Text(topAlertMessage)
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
    
    // MARK: - Validation Properties
    private var hasMinLength: Bool { password.count >= 8 }
    private var hasUppercase: Bool { password.range(of: "[A-Z]", options: .regularExpression) != nil }
    private var hasNumber: Bool { password.range(of: "[0-9]", options: .regularExpression) != nil }
    private var hasSymbol: Bool { password.range(of: "[^A-Za-z0-9]", options: .regularExpression) != nil }
    
    private var isPasswordValid: Bool {
        hasMinLength && hasUppercase && hasNumber && hasSymbol
    }
    
    func validateAndProceed() {
        if name.isEmpty || surname.isEmpty || tcKimlik.isEmpty || email.isEmpty || password.isEmpty || confirmPassword.isEmpty || phone.isEmpty {
            topAlertMessage = "Please fill in all fields."
            showTopAlert = true
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
            return
        }
        
        if !isValidEmail(email) {
            topAlertMessage = "Please enter a valid email address."
            showTopAlert = true
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
            return
        }
        
        if tcKimlik.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            topAlertMessage = "National ID is required."
            showTopAlert = true
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
            return
        }
        
        let phoneDigits = phone.filter { $0.isNumber }
        if phoneDigits.count != 10 {
            topAlertMessage = "Phone number must be 10 digits."
            showTopAlert = true
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
            return
        }
        
        if password != confirmPassword {
            topAlertMessage = "Passwords do not match."
            showTopAlert = true
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
            return
        }
        
        guard isPasswordValid else {
            topAlertMessage = "Please ensure your password meets all requirements."
            showTopAlert = true
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
            return
        }
        
        registerUser()
    }

    func registerUser() {
        guard let url = URL(string: "http://34.32.107.243:8080/api/customer/register") else { return }
        let msisdn = phone.filter { $0.isNumber }
        var components = URLComponents(url: url, resolvingAgainstBaseURL: false)!
        components.queryItems = [
            URLQueryItem(name: "msisdn", value: msisdn),
            URLQueryItem(name: "name", value: name.trimmingCharacters(in: .whitespacesAndNewlines)),
            URLQueryItem(name: "surname", value: surname.trimmingCharacters(in: .whitespacesAndNewlines)),
            URLQueryItem(name: "email", value: email.trimmingCharacters(in: .whitespacesAndNewlines)),
            URLQueryItem(name: "password", value: password),
            URLQueryItem(name: "nationalId", value: tcKimlik)
        ]
        print("Request URL: \(components.url!)")
        var request = URLRequest(url: components.url!)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    self.topAlertMessage = "Registration failed: \(error.localizedDescription)"
                    self.showTopAlert = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) { self.showTopAlert = false }
                    return
                }
                if let data = data, let str = String(data: data, encoding: .utf8) {
                    print("API response: \(str)")
                }
                guard let httpResponse = response as? HTTPURLResponse else {
                    self.topAlertMessage = "Registration failed: No response."
                    self.showTopAlert = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) { self.showTopAlert = false }
                    return
                }
                if httpResponse.statusCode == 200 || httpResponse.statusCode == 201 {
                    self.showChooseTariff = true
                } else {
                    self.topAlertMessage = "Registration failed: \(HTTPURLResponse.localizedString(forStatusCode: httpResponse.statusCode))"
                    self.showTopAlert = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) { self.showTopAlert = false }
                }
            }
        }
        task.resume()
    }
    
    func isValidEmail(_ email: String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPred = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailPred.evaluate(with: email)
    }
}

struct PasswordRequirementRow: View {
    let met: Bool
    let text: String
    let showError: Bool

    var body: some View {
        HStack(spacing: 8) {
            Image(systemName: iconName)
                .foregroundColor(iconColor)
            Text(text)
                .foregroundColor(met ? .primary : .secondary)
                .font(.subheadline)
        }
    }

    private var iconName: String {
        if met {
            return "checkmark.circle.fill"
        } else if showError {
            return "xmark.circle.fill"
        } else {
            return "circle"
        }
    }

    private var iconColor: Color {
        if met {
            return .green
        } else if showError {
            return .red
        } else {
            return .gray
        }
    }
}

struct SignupPage_Previews: PreviewProvider {
    static var previews: some View {
        SignupPage()
    }
} 
