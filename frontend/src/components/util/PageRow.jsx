const PageRow = (props) => {
    const {totalPages, currentPage, onPageChange} = props;
    const pageNums = Array.from({length: totalPages}, (_, i) => i + 1);
    return (
      <nav aria-label="Page navigation example">
        <ul className="pagination justify-content-center">
          {pageNums.map(p => (
            <li key={p} className={"page-item" + (p == currentPage ? " active": "" )} onClick={() => onPageChange(p)}>
              <span className="page-link">{p}</span>
            </li>
          ))}
        </ul>
      </nav>);
      
  }

  export default PageRow;