package hashing;

public class Book {
    private MyString title;
    private MyString author;
    private ISBN10 isbn;
    private MyString content;
    private int price;
    private int cache;

    public Book(String title, String author, String isbn, String content, int price) {
        this.title = new MyString(title);
        this.author = new MyString(author);
        this.isbn = new ISBN10(isbn);
        this.content = new MyString(content);
        this.price = price;
        this.cache = -1;
    }

    public MyString getTitle() {
        return title;
    }

    public MyString getAuthor() {
        return author;
    }

    public ISBN10 getIsbn() {
        return isbn;
    }

    public MyString getContent() {
        return content;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    /*
    Vi tänker att en lösning som gör hashcoden snabb är att man räknar ut hashcoden en gång och
    lägger den i en "cache", sen returnerar man bara cachen hela tiden när hashCode efterfrågas.
    Hashcoden vi har gjort är baserat på ISBN-numret eftersom det är unikt samt att alla ISBN-nummer
    är relativt olika varandra. Anledningen till att instansvariabeln cache instansieras till -1 i början är för
    att detta är ett nummer hashcoden inte kommer returnera någonsin, så det blir som ett temporärt värde som bara är det för
    att flagga att hashcoden ska räknas ut första gången.

    i Hashcoden får vi ut ISBN-numret som en int, sen tar vi ISBN-numret och gångrar det med ett primtal vi valt till 17.
    Om det talet som kommer ut mot förmodan skulle bli negativt görs en en koll och med hjälp av bitmanipulering tvingar
    vi det till att bli positivt.

    Den lösning kräver förstås också att instansvariabeln cache aldrig får ändras efter hashcode tillkallats.
     */
    @Override
    public int hashCode() {
        if (cache == -1) {
            cache = this.isbn.hashCode();
            return cache;
        }

        return cache;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Book) {
            return this.isbn.equals(((Book) obj).getIsbn());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("\"%s\" by %s Price: %d ISBN: %s length: %s", title, author, price, isbn,
                content.length());
    }

}
