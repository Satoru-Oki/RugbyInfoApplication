window.addEventListener('DOMContentLoaded', (event) => {
    const sortPositionGroups = (table) => {
        let rows = Array.from(table.querySelectorAll('tbody > tr'));

        // FWとBKの順番でソートする
        rows.sort((rowA, rowB) => {
            let positionA = rowA.querySelector('td:first-child').textContent.trim();
            let positionB = rowB.querySelector('td:first-child').textContent.trim();

            if (positionA === 'FW' && positionB === 'BK') {
                return -1; // FWを先に
            } else if (positionA === 'BK' && positionB === 'FW') {
                return 1; // BKを後に
            } else {
                return 0; // その他の場合はそのまま
            }
        });

        // ソートされた行をテーブルに追加
        let tbody = table.querySelector('tbody');
        rows.forEach(row => tbody.appendChild(row));
    };

    // 全てのテーブルに対してソートを実行
    document.querySelectorAll('table[id^="table-"]').forEach(sortPositionGroups);
});
