package CGF;
//Kafka'nin abone olan sinifi sayesinde
//import kafka.Message_name
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


         /*
          * kafka'Dan gelen mesajlari sürekli okumak için bir while
          döngüsü ekliyorum
          */
          while(true) {
            records<key,value> records = subs.poll(); //yeni mesaji almak icin
            for(records<key,value> record: records) {
                Message_name x = record.value();
                //gelen mesaji, oracle veritabanina da yazmaliyim..
                Oracle.callInsertProcedure(x);
            }
            //key= id gibi
            //value= mesajin içerigi
            //records= kafka'dan gelen tekil mesaj
          }
    }
}
