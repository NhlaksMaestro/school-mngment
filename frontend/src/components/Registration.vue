<template>
  <div :show="alertShow" :class="alertClass">
    <Alert />
    {{ alertMessage }}
  </div>
  <div>
    <h4 class="mt-4" v-if="existingUserId > 0">Update Existing Student</h4>
    <h4 class="mt-4" v-else>Create new Student</h4>
    <form @submit.prevent="registerUser" class="mt-4">
      <div class="form-row">
        <div class="form-group col-md-6">
          <label for="inputFirstName">First Name</label>
          <input type="text" class="form-control" id="inputFirstName" placeholder="First Name" v-model="firstName"
            :class="{ 'is-invalid': !firstName && submitted }">
          <div class="invalid-feedback">First Name is required.</div>
          <div class="invalid-feedback" v-if="submitted && !isFirstNameValid">
            First Name must contain only letters.
          </div>
        </div>
        <div class="form-group col-md-6">
          <label for="inputLastName">Last Name</label>
          <input type="text" class="form-control" id="inputLastName" placeholder="Last Name" v-model="lastName"
            :class="{ 'is-invalid': !lastName && submitted }">
          <div class="invalid-feedback">Last Name is required.</div>
          <div class="invalid-feedback" v-if="submitted && !isLastNameValid">
            Last Name must contain only letters.
          </div>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-2">
          <label for="inputCountryCode">Mobile</label>
          <select id="inputCountryCode" class="form-control" v-model="selectCountryCode"
            :class="{ 'is-invalid': !selectCountryCode && submitted }">
            <option disabled value="">Select Country Code</option>
            <option v-for="country in countryCodes" :value="country.code" :key="country.code">
              {{ country.country }} ({{ country.iso }})
            </option>
          </select>
          <div class="invalid-feedback">Country Code is required.</div>
        </div>
        <div class="form-group col-md-4">
          <label for="inputCellphone">Number</label>
          <input type="text" class="form-control" id="inputCellphone" v-model="cellphone"
            :disabled="selectCountryCode === ''" :class="{ 'is-invalid': !cellphone && submitted }">
          <div class="invalid-feedback">Cellphone is required.</div>
          <div class="invalid-feedback" v-if="submitted && !isCellphoneValid">
            Phone number must be a numeric value.
          </div>
        </div>
        <div class="form-group col-md-6">
          <label for="inputEmail4">Email</label>
          <input type="email" class="form-control" id="inputEmail4" placeholder="Email" v-model="email"
            :class="{ 'is-invalid': !email && submitted }">
          <div class="invalid-feedback">Email is required.</div>
          <div class="invalid-feedback" v-if="submitted && !isEmailValid">
            Please enter a valid email address.
          </div>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-6">
          <label for="inputDateOfBirth">Date Of Birth</label>
          <input type="date" class="form-control" id="inputDateOfBirth" placeholder="Date Of Birth" v-model="dateOfBirth"
            :class="{ 'is-invalid': !dateOfBirth && submitted }">
          <div class="invalid-feedback">Date of Birth is required.</div>
        </div>
        <div class="form-group col-md-6">
          <label for="inputCurrentScore">Current Score</label>
          <input type="number" class="form-control" id="inputCurrentScore" placeholder="Current Score" v-model="currScore"
            min="0" max="100">
          <div class="invalid-feedback" v-if="submitted && (currScore < 0 || currScore > 100)">
            Current Score must be a number between 0 and 100.
          </div>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-6">
          <button type="submit" class="btn btn-primary form-control" v-if="existingUserId > 0">Update</button>
          <button type="submit" class="btn btn-primary form-control" v-else>Save</button>
        </div>
        <div class="form-group col-md-6">
          <button type="submit" class="btn btn-primary form-control" @click="cancelRegistration">Cancel</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import countries from '@/stores/countryphonecodes.json';
import Alert from '@/components/Alert.vue';

export default {
  data() {
    return {
      firstName: '',
      lastName: '',
      selectCountryCode: '',
      cellphone: '',
      email: '',
      dateOfBirth: '',
      currScore: '',
      countryCodes: [],
      submitted: false,
      isFirstNameValid: true,
      isLastNameValid: true,
      isCellphoneValid: true,
      isEmailValid: true,
      isFormValid: false,
      alertShow: false,
      alertClass: '',
      alertMessage: '',
      existingUserId: 0
    };
  },
  components: {
    Alert
  },
  created() {
    this.countryCodes = countries;
    // Extract the user ID from the route parameter
    this.existingUserId = this.$route.params.userId;
    if (this.existingUserId != null | undefined && this.existingUserId > 0) {
      // Fetch the existing user data using the endpoint
      fetch(`http://localhost:700/users/${this.existingUserId}`)
        .then(response => {
          if (response.ok) {
            return response.json();
          } else {
            console.log('error', 'Error retrieving user data');
            this.showAlert('error', `Error retrieving user ${this.existingUserId}`);
          }
        })
        .then(userData => {
          this.firstName = userData.firstName;
          this.lastName = userData.lastName;
          if (userData.cellphone.indexOf(' ') >= 0) {
            this.selectCountryCode = userData.cellphone.split(' ')[0].split("+").join("");
          } else {
            this.selectCountryCode = ".";
          };
          this.cellphone = userData.cellphone;
          this.email = userData.email;
          this.dateOfBirth = userData.dateOfBirth;
          this.currScore = userData.currScore;
        })
        .catch(error => {
          console.error(`Error retrieving user ${this.existingUserId}`, error);
          this.showAlert('error', `Error retrieving user ${this.existingUserId}`);
        });
    }
  },
  watch: {
    selectCountryCode(newValue) {
      if (this.existingUserId && this.existingUserId > 0) {
        this.cellphone = `${this.cellphone}`;
      } else {
        this.cellphone = "";
        if (newValue) {
          this.cellphone = `+${newValue} ${this.cellphone}`;
        }
      }
    },
  },
  methods: {
    registerUser() {
      this.validateForm();
      this.submitted = true;
      if (this.isFormValid) {
        this.submitted = true;
        if (!this.existingUserId || this.existingUserId <= 0) {
          const newUser = {
            firstName: this.firstName,
            lastName: this.lastName,
            countryCode: this.selectCountryCode,
            cellphone: this.cellphone,
            email: this.email,
            dateOfBirth: this.dateOfBirth,
            currScore: this.currScore
          };
          fetch('http://localhost:700/users', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(newUser),
          })
            .then(response => {
              if (response.ok) {
                console.log('User registered successfully!');
                this.showAlert('success', `User ${this.firstName} ${this.lastName} registered successfully!`);
              } else {
                console.error('Error registering user:', response.statusText);
                this.showAlert('error', 'Error registering user: ' + response.statusText);
              }
            })
            .catch(error => {
              console.error('Error registering user:', error);
              this.showAlert('error', 'Error registering user: ' + error);
              // Handle any other errors that occurred during the request
            });
        } else {
          const updatedUser = {
            firstName: this.firstName,
            lastName: this.lastName,
            countryCode: this.selectCountryCode,
            cellphone: this.cellphone,
            email: this.email,
            dateOfBirth: this.dateOfBirth
          };
          fetch(`http://localhost:700/users/${this.existingUserId}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedUser),
          })
            .then(response => {
              if (response.ok) {
                console.log('User updated successfully!');
                this.showAlert('success', `User ${this.firstName} ${this.lastName} updated successfully!`);
              } else {
                console.error('Error updating user:', response.statusText);
                this.showAlert('error', 'Error updating user: ' + response.statusText);
              }
            })
            .catch(error => {
              console.error('Error updating user:', error);
              this.showAlert('error', 'Error updating user: ' + error);
            });
        }
      } else {
        this.showAlert('error', 'Form validation failed. Please check the entered values.');
      }
    },
    validateForm() {
      this.isFirstNameValid = /^[A-Za-z]+$/.test(this.firstName);
      this.isLastNameValid = /^[A-Za-z]+$/.test(this.lastName);
      this.isCellphoneValid = /^[\d+\-, ]+$/.test(this.cellphone);
      this.isEmailValid = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(this.email);
      this.isFormValid = this.isFirstNameValid && this.isLastNameValid && this.isCellphoneValid && this.isEmailValid;
    },
    cancelRegistration() {
      this.$router.push('/search');
    },
    showAlert(type, message) {
      this.alertClass = 'alert alert-' + type;
      this.alertMessage = message;
      this.alertShow = true;
      setTimeout(() => {
        this.alertShow = false;
        this.alertClass = "";
        this.alertMessage = '';
        setTimeout(() => {
          if (type === 'success') {
            this.cancelRegistration();
          }
        }, 1000);
      }, 3000);
    }
  }
};
</script>

<style scoped>
/* Add your custom styles here */
</style>