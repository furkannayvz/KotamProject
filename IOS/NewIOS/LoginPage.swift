import SwiftUI

struct LoginPage: View {
    @State private var phoneNumber: String = ""
    @State private var password: String = ""
    @State private var showAlert: Bool = false
    @State private var alertMessage: String = ""
    @State private var isLoggedIn: Bool = false
    @State private var showSignup: Bool = false
    @State private var showForgotPassword: Bool = false
    @State private var showDashboard: Bool = false
    @State private var showPassword: Bool = false
    @State private var phoneTouched = false
    @FocusState private var phoneInputFocused: Bool
    @State private var showTopAlert = false
    @State private var topAlertMessage = ""
   
    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                VStack(spacing: 4) {
                    Image("Logo")
                        .resizable()
                        .scaledToFit()
                        .frame(height: 48)
                        .padding(.top, 24)
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
                Text("WELCOME TO\nKOTAM")
                    .font(.system(size: 32, weight: .bold))
                    .multilineTextAlignment(.leading)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 8)
                Text("Track every GB you use, every minute you\nspend with Kotam.")
                    .font(.body)
                    .foregroundColor(.gray)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.bottom, 24)
      
                VStack(alignment: .leading, spacing: 16) {
                    Text("Phone Number").font(.headline).padding(.bottom, -8)
                    ZStack(alignment: .trailing) {
                        TextField("Your Phone Number", text: $phoneNumber, onEditingChanged: { editing in
                            if !editing { phoneTouched = true }
                        })
                            .padding(.trailing, 36)
                        .padding()
                        .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                    .keyboardType(.phonePad)
                            .focused($phoneInputFocused)
                        Image(systemName: "checkmark.circle")
                            .foregroundColor(
                                phoneNumber.count == 10 ? Color.green :
                                (phoneTouched && !phoneInputFocused ? Color.red : Color.gray.opacity(0.4))
                            )
                            .padding(.trailing, 16)
                            .opacity(phoneNumber.isEmpty ? 0 : 1)
                    }
                    Spacer().frame(height: 0)
                    Text("Password").font(.headline).padding(.bottom, -8)
                    ZStack(alignment: .trailing) {
                        if showPassword {
                            TextField("Your Password", text: $password)
                                .padding()
                                .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                        } else {
                            SecureField("Your Password", text: $password)
                                .padding()
                                .background(RoundedRectangle(cornerRadius: 8).stroke(Color.gray, lineWidth: 1))
                        }
                        if !password.isEmpty {
                            Button(action: { showPassword.toggle() }) {
                                Image(systemName: showPassword ? "eye" : "eye.slash")
                                    .foregroundColor(.gray)
                                    .padding(.trailing, 12)
                            }
                        }
                    }
                }
                .padding(.bottom, 8)

                HStack {
                    Spacer()
                    Button(action: { showForgotPassword = true }) {
                        Text("Forgot password?")
                        .font(.footnote)
                            .foregroundColor(Color(red: 27/255, green: 38/255, blue: 69/255))
                            .bold()
                    }
                }
                .padding(.bottom, 16)
                NavigationLink(destination: ForgotPasswordPage(), isActive: $showForgotPassword) { EmptyView() }
                Button(action: {
                    if phoneNumber.isEmpty || password.isEmpty {
                        topAlertMessage = "Please fill in all fields."
                        showTopAlert = true
                        DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
                    } else if !isValidPhoneNumber(phoneNumber) {
                        topAlertMessage = "Please enter a valid phone number."
                        showTopAlert = true
                        DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
                    } else {
                        loginUser()
                    }
                }) {
                    Text("Login")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color(red: 27/255, green: 38/255, blue: 69/255))
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .font(.title3)
                }
                .padding(.bottom, 8)

                Button(action: { showSignup = true }) {
                    Text("Sign Up")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color(red: 80/255, green: 82/255, blue: 90/255))
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .font(.title3)
                }
                .padding(.bottom, 8)
                NavigationLink(destination: SignupPage(), isActive: $showSignup) { EmptyView() }
                NavigationLink(destination: RemainingUsagePage(msisdn: phoneNumber.filter { $0.isNumber }), isActive: $isLoggedIn) { EmptyView() }
                Spacer()
            }
            .padding(.horizontal, 24)
            .alert(isPresented: $showAlert) {
                Alert(title: Text("Uyarı"), message: Text(alertMessage), dismissButton: .default(Text("Tamam")))
            }
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
    }
    
    func isValidPhoneNumber(_ number: String) -> Bool {
        let digits = number.filter { $0.isNumber }
        return digits.count == 10 || digits.count == 11
    }
    
    func loginUser() {
        guard let url = URL(string: "http://34.14.39.115/api/customer/login") else { return }
        let msisdn = phoneNumber.filter { $0.isNumber }
        let body: [String: Any] = [
            "msisdn": msisdn,
            "password": password
        ]
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try? JSONSerialization.data(withJSONObject: body)
        URLSession.shared.dataTask(with: request) { data, response, error in
            DispatchQueue.main.async {
                if let error = error {
                    topAlertMessage = "An error occurred: \(error.localizedDescription)"
                    showTopAlert = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
                    return
                }
                if let data = data, let str = String(data: data, encoding: .utf8) {
                    print("Login API response: \(str)")
                }
                guard let httpResponse = response as? HTTPURLResponse else {
                    topAlertMessage = "Server error."
                    showTopAlert = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
                    return
                }
                if httpResponse.statusCode == 200 {
                    isLoggedIn = true
                } else {
                    topAlertMessage = "User not found or credentials are incorrect."
                    showTopAlert = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 2) { showTopAlert = false }
                }
            }
        }.resume()
    }
}

struct LoginPage_Previews: PreviewProvider {
    static var previews: some View {
        LoginPage()
    }
} 
