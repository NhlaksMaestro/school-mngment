<template>
    <div>
      <div v-if="showAlert" :class="alertClass" role="alert">
        {{ alertMessage }}
      </div>
  
      <!-- Rest of your page content -->
  
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        showAlert: false,
        alertClass: '',
        alertMessage: ''
      };
    },
    methods: {
      showSuccessAlert(message) {
        this.showAlert = true;
        this.alertClass = 'alert alert-success';
        this.alertMessage = message;
        this.hideAlertAfterDelay();
      },
      showErrorAlert(message) {
        this.showAlert = true;
        this.alertClass = 'alert alert-danger';
        this.alertMessage = message;
        this.hideAlertAfterDelay();
      },
      hideAlertAfterDelay() {
        setTimeout(() => {
          this.showAlert = false;
        }, 1000); // Adjust the delay time (in milliseconds) as needed
      }
    }
  };
  </script>
  