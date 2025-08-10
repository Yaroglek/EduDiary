// Скрипты для admin users.html
function selectRole(role) {
    closeAllModals(); // Закрываем модальное окно выбора роли
    loadUsers(role);
    showModal('listUsersModal');
}

function loadUsers(role) {
    fetch('/admin/users/list?role=' + role)
        .then(res => res.json())
        .then(users => {
            const tbody = document.getElementById('usersTableBody');
            tbody.innerHTML = '';
            users.forEach(user => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                        <td>${user.username}</td>
                        <td>${user.fullName}</td>
                        <td>${user.email}</td>
                        <td><button class="btn danger" onclick="deleteUser(${user.id}, '${role}')">Удалить</button></td>
                    `;
                tbody.appendChild(tr);
            });
        });
}

function deleteUser(userId, role) {
    if (!confirm('Вы действительно хотите удалить пользователя?')) return;

    fetch('/admin/users/delete/' + userId, {method: 'DELETE'})
        .then(res => {
            if (res.ok) {
                loadUsers(role);
            } else {
                alert('Ошибка при удалении пользователя');
            }
        });
}

// Скрипты для subjects.html
function deleteSubject(subjectId) {
    if (!confirm('Вы действительно хотите удалить предмет?')) return;

    fetch('/admin/subjects/delete/' + subjectId, { method: 'DELETE' })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('Ошибка при удалении предмета');
            }
        })
        .catch(() => alert('Ошибка при удалении предмета'));
}

// Скрипты для class-subjects.html
document.addEventListener('DOMContentLoaded', function() {
    const subjectSelect = document.getElementById('subjectSelect');
    if (subjectSelect) {
        subjectSelect.addEventListener('change', function () {
            const subjectId = this.value;
            const teacherSelect = document.getElementById('teacherSelect');
            teacherSelect.innerHTML = '<option value="" disabled selected>Загрузка...</option>';

            fetch('/admin/classes/subjects/' + subjectId + '/teachers')
                .then(res => res.json())
                .then(teachers => {
                    teacherSelect.innerHTML = '';
                    if (teachers.length > 0) {
                        teachers.forEach(t => {
                            const opt = document.createElement('option');
                            opt.value = t.id;
                            opt.text = t.fullName;
                            teacherSelect.add(opt);
                        });
                    } else {
                        const opt = document.createElement('option');
                        opt.text = 'Учителя не найдены';
                        teacherSelect.add(opt);
                    }
                })
                .catch(() => {
                    teacherSelect.innerHTML = '';
                    const opt = document.createElement('option');
                    opt.text = 'Ошибка загрузки';
                    teacherSelect.add(opt);
                });
        });

        // Инициализируем загрузку учителей, если предмет уже выбран
        if (subjectSelect.value) {
            subjectSelect.dispatchEvent(new Event('change'));
        }
    }
});

// Скрипты для admin/schedule.html
document.addEventListener('DOMContentLoaded', function() {
    const classSelect = document.getElementById("classSelect");
    const lessonSelects = document.querySelectorAll(".lesson-select");
    const scheduleForm = document.getElementById("scheduleForm");

    if (classSelect) {
        classSelect.addEventListener("change", function () {
            const classId = this.value;
            fetch(`/admin/classes/${classId}/subjects-list`)
                .then(res => res.json())
                .then(subjects => {
                    lessonSelects.forEach(select => {
                        select.innerHTML = '<option value="" selected disabled>Выберите предмет</option>';
                        subjects.forEach(subject => {
                            const option = document.createElement('option');
                            option.value = subject.id;
                            option.text = subject.subjectName + ' (' + subject.teacherName + ')';
                            select.appendChild(option);
                        });
                    });
                });
        });
    }

    if (scheduleForm) {
        scheduleForm.addEventListener("submit", function (e) {
            e.preventDefault();

            const classId = classSelect.value;
            const dayOfWeek = document.getElementById("daySelect").value;
            const lessons = [];

            lessonSelects.forEach((select, index) => {
                if (select.value) {
                    lessons.push({
                        lessonNumber: index + 1,
                        classSubjectId: select.value
                    });
                }
            });

            fetch("/api/schedule/generate?classId=" + classId + "&dayOfWeek=" + dayOfWeek, {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(lessons)
            }).then(res => {
                if (res.ok) alert("Расписание сохранено");
                else alert("Ошибка при сохранении расписания");
            });
        });
    }
});

