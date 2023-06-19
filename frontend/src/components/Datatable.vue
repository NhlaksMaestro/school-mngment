<template>
  <div>
    <div :show="alertShow" :class="alertClass">
      <Alert />
      {{ alertMessage }}
    </div>
    <table class="table table-bordered">
      <thead>
        <tr>
          <th>Student No.</th>
          <th>Full Name</th>
          <th>Cell No.</th>
          <th>Email</th>
          <th>Current Score</th>
          <th>Average Score</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in paginatedData" :key="index">
          <td>{{ item.studentNumber }}</td>
          <td>{{ item.firstName }} {{ item.lastName }}</td>
          <td>{{ item.cellphone }}</td>
          <td>{{ item.email }}</td>
          <td>{{ item.currScore }}</td>
          <td>{{ item.aveScore }}</td>
          <td>
            <button class="btn btn-success btn-sm" @click="addScore(item)">
              Add Score
            </button>
            <button class="btn btn-primary btn-sm" @click="editItem(item)">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-fill"
                viewBox="0 0 16 16">
                <path
                  d="M12.854.146a.5.5 0 0 0-.707 0L10.5 1.793 14.207 5.5l1.647-1.646a.5.5 0 0 0 0-.708l-3-3zm.646 6.061L9.793 2.5 3.293 9H3.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.207l6.5-6.5zm-7.468 7.468A.5.5 0 0 1 6 13.5V13h-.5a.5.5 0 0 1-.5-.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.5-.5V10h-.5a.499.499 0 0 1-.175-.032l-.179.178a.5.5 0 0 0-.11.168l-2 5a.5.5 0 0 0 .65.65l5-2a.5.5 0 0 0 .168-.11l.178-.178z" />
              </svg>
            </button>
            <button class="btn btn-danger btn-sm bi bi-0-circle-fill" @click="deleteItem(item)">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash"
                viewBox="0 0 16 16">
                <path
                  d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5Zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6Z" />
                <path
                  d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1ZM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118ZM2.5 3h11V2h-11v1Z" />
              </svg>
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <nav aria-label="Page navigation">
      <ul class="pagination">
        <li class="page-item" :class="{ active: page === currentPage }" v-for="page in totalPages" :key="page">
          <button class="page-link" @click="setCurrentPage(page)">{{ page }}</button>
        </li>
      </ul>
    </nav>
    <div>Number of pages: {{ totalPages }}</div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      data: {
        items: [],
        total: 0
      },
      perPage: 5,
      currentPage: 1,
      searchText: "",
      sortBy: "firstName",
      url: "",
      endpoint: "http://localhost:700/users",
      alertShow: false,
      alertClass: '',
      alertMessage: '',
      userId: 0
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.data.items.length / this.perPage);
    },
    paginatedData() {
      if (this.data.items === null | undefined || this.data.items.length === 0) {
        return [];
      }
      const start = (this.currentPage - 1) * this.perPage;
      const end = start + this.perPage;
      return this.data.items.slice(start, end);
    }
  },
  created() {
    this.fetchData();
  },
  methods: {
    determineUrl() {
      this.url = this.endpoint + "?" + "sortBy=" + this.sortBy + "&limit=" + this.perPage;
      if (this.searchText === null | undefined) {
        this.url += "&search=" + this.searchText
      }
      return this.url;
    },
    fetchData() {
      var url = this.determineUrl();
      fetch(url)
        .then(response => response.json())
        .then(data => {
          this.data = data;
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
    },
    setCurrentPage(page) {
      this.currentPage = page;
    },
    addScore(item) {
      console.log('Add score for:', item);
    },
    deleteItem(item) {
      console.log('Delete item:', item);
      const userId = item.id;
      const deleteUrl = `${this.endpoint}/${userId}`;
      fetch(deleteUrl, {
        method: 'DELETE'
      })
        .then(response => {
          if (response.status === 204) {
            console.log('User Deleted successfully!');
            this.showAlert('success', `User with id: ${userId} deleted successfully!`);
          } else {
            console.error('Error deleting user:', response.statusText);
            this.showAlert('error', 'Error deleting user: ' + response.statusText);
          }
        })
        .catch(error => {
          console.error('Error deleting user:', error);
          this.showAlert('error', `User with id: ${userId} failed to delete!`);
        });
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
            window.location.reload();
          }
        }, 1000);
      }, 3000);
    },
    editItem(item) {
      console.log('Edit item:', item);
      const userId = item.id;
      this.$router.push({ name: 'edit', params: { userId: userId } });
    }
  }
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
