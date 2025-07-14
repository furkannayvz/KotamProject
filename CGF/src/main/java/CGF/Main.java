package CGF;
//Kafka'nin abone olan sinifi sayesinde

public class Main {
    public static void main(String[] args) {
        Subs<Message_name> subs = new Subs<>();
        //Subs=> kafka içerisinde yer alan, kafka abone sinifi.
        //message_name=> alınacak mesaj değişkenin adi.
        /*subs nesnesini oluşturdum. bu nesnenin içeriğini kafka sınıfında
        yer alacak olan Subs sınıfının ilgili metodunu kullanarak
        dolduracağım (kafka'dan veriyi alabilmek için). örnek:xyz func.
        */
        subs.xyz();

        /*
         * böylelikle daha önceden kafka'ya serializer yöntemi ile
         * yollanan bilgiyi, deserializer ile çekmiş olacağım.
         * Oracle ve diğer işlemlere 15.07.2025 tarihinde bakacağım.
         */
    }
}
