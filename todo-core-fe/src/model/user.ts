class User {
    constructor(private _id: number,
                private _username: string,
                private _firstName: string | null,
                private _lastName: string | null,
                private _email: string,
                private _darkMode: boolean) {
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get username(): string {
        return this._username;
    }

    set username(value: string) {
        this._username = value;
    }

    get firstName(): string | null {
        return this._firstName;
    }

    set firstName(value: string | null) {
        this._firstName = value;
    }

    get lastName(): string | null {
        return this._lastName;
    }

    set lastName(value: string | null) {
        this._lastName = value;
    }

    get email(): string {
        return this._email;
    }

    set email(value: string) {
        this._email = value;
    }

    get darkMode(): boolean {
        return this._darkMode;
    }

    set darkMode(value: boolean) {
        this._darkMode = value;
    }
}
