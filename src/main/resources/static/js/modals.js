function showModal(id) {
    document.getElementById(id).style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';
}

function closeModal(id) {
    document.getElementById(id).style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
}

function closeAllModals() {
    document.querySelectorAll('.modal').forEach(m => m.style.display = 'none');
    document.getElementById('modalOverlay').style.display = 'none';
}

function openParentModal(studentId) {
    document.getElementById('modalStudentId').value = studentId;
    showModal('parentModal');
}
