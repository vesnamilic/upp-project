export class Registration {
    constructor(private username, private password, private firstName, private lastName, private city,
         private country, private title, private email, private requestedReviewerRole, private scientificAreas) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.title = title;
        this.email = email;
        this.requestedReviewerRole = requestedReviewerRole;
        this.scientificAreas = scientificAreas;

      }
}
